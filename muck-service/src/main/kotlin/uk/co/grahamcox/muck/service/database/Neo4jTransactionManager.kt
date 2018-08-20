package uk.co.grahamcox.muck.service.database

import org.neo4j.driver.v1.Driver
import org.neo4j.driver.v1.Session
import org.neo4j.driver.v1.Transaction
import org.slf4j.LoggerFactory
import org.springframework.transaction.CannotCreateTransactionException
import org.springframework.transaction.NoTransactionException
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.TransactionException
import org.springframework.transaction.support.AbstractPlatformTransactionManager
import org.springframework.transaction.support.DefaultTransactionStatus

/**
 * Transaction Manager for working with Neo4J Transactions
 */
class Neo4jTransactionManager(private val driver: Driver) : AbstractPlatformTransactionManager() {
    companion object {
        /** The logger to use */
        private val LOG = LoggerFactory.getLogger(Neo4jTransactionManager::class.java)
    }

    /**
     * Wrapper around the Neo4J Transaction and Session
     */
    class Neo4jTransaction(private val driver: Driver) {
        /** The network session */
        var session: Session? = null
        /** The database transaction */
        var transaction: Transaction? = null

        /**
         * Start the transaction
         */
        fun start() {
            if (session != null) {
                throw CannotCreateTransactionException("Transaction already started")
            }

            session = driver.session()
            transaction = session?.beginTransaction()
        }
    }

    /** The cache of transactions for each thread */
    private val transactionCache = ThreadLocal<Neo4jTransaction>()

    /**
     * Return a transaction object for the current transaction state.
     *
     * The returned object will usually be specific to the concrete transaction
     * manager implementation, carrying corresponding transaction state in a
     * modifiable fashion. This object will be passed into the other template
     * methods (e.g. doBegin and doCommit), either directly or as part of a
     * DefaultTransactionStatus instance.
     *
     * The returned object should contain information about any existing
     * transaction, that is, a transaction that has already started before the
     * current `getTransaction` call on the transaction manager.
     * Consequently, a `doGetTransaction` implementation will usually
     * look for an existing transaction and store corresponding state in the
     * returned transaction object.
     * @return the current transaction object
     * @throws org.springframework.transaction.CannotCreateTransactionException
     * if transaction support is not available
     * @throws TransactionException in case of lookup or system errors
     * @see .doBegin
     *
     * @see .doCommit
     *
     * @see .doRollback
     *
     * @see DefaultTransactionStatus.getTransaction
     */
    override fun doGetTransaction(): Any {
        if (transactionCache.get() == null) {
            val neo4jTransaction = Neo4jTransaction(driver)
            LOG.debug("Creating a new transaction: {}", neo4jTransaction)
            transactionCache.set(neo4jTransaction)
        }
        return transactionCache.get()
    }

    /**
     * Check if the given transaction object indicates an existing transaction
     * (that is, a transaction which has already started).
     *
     * The result will be evaluated according to the specified propagation
     * behavior for the new transaction. An existing transaction might get
     * suspended (in case of PROPAGATION_REQUIRES_NEW), or the new transaction
     * might participate in the existing one (in case of PROPAGATION_REQUIRED).
     *
     * The default implementation returns `false`, assuming that
     * participating in existing transactions is generally not supported.
     * Subclasses are of course encouraged to provide such support.
     * @param transaction transaction object returned by doGetTransaction
     * @return if there is an existing transaction
     * @throws TransactionException in case of system errors
     * @see .doGetTransaction
     */
    override fun isExistingTransaction(transaction: Any): Boolean {
        return (transaction as Neo4jTransaction).transaction != null
    }

    /**
     * Begin a new transaction with semantics according to the given transaction
     * definition. Does not have to care about applying the propagation behavior,
     * as this has already been handled by this abstract manager.
     *
     * This method gets called when the transaction manager has decided to actually
     * start a new transaction. Either there wasn't any transaction before, or the
     * previous transaction has been suspended.
     *
     * A special scenario is a nested transaction without savepoint: If
     * `useSavepointForNestedTransaction()` returns "false", this method
     * will be called to start a nested transaction when necessary. In such a context,
     * there will be an active transaction: The implementation of this method has
     * to detect this and start an appropriate nested transaction.
     * @param transaction transaction object returned by `doGetTransaction`
     * @param definition TransactionDefinition instance, describing propagation
     * behavior, isolation level, read-only flag, timeout, and transaction name
     * @throws TransactionException in case of creation or system errors
     */
    override fun doBegin(transaction: Any, definition: TransactionDefinition) {
        LOG.debug("Starting transaction: {}", transaction)
        (transaction as Neo4jTransaction).start()
    }

    /**
     * Perform an actual commit of the given transaction.
     *
     * An implementation does not need to check the "new transaction" flag
     * or the rollback-only flag; this will already have been handled before.
     * Usually, a straight commit will be performed on the transaction object
     * contained in the passed-in status.
     * @param status the status representation of the transaction
     * @throws TransactionException in case of commit or system errors
     * @see DefaultTransactionStatus.getTransaction
     */
    override fun doCommit(status: DefaultTransactionStatus) {
        val transaction = unwrapTransaction(status)
        LOG.debug("Committing transaction: {}", transaction)

        if (transaction.transaction == null) {
            throw NoTransactionException("No current transaction")
        }
        transaction.transaction?.success()
        transaction.transaction?.close()
        transaction.session?.close()
        transactionCache.set(null)
    }

    /**
     * Perform an actual rollback of the given transaction.
     *
     * An implementation does not need to check the "new transaction" flag;
     * this will already have been handled before. Usually, a straight rollback
     * will be performed on the transaction object contained in the passed-in status.
     * @param status the status representation of the transaction
     * @throws TransactionException in case of system errors
     * @see DefaultTransactionStatus.getTransaction
     */
    override fun doRollback(status: DefaultTransactionStatus) {
        val transaction = unwrapTransaction(status)
        LOG.debug("Rolling back transaction: {}", transaction)

        if (transaction.transaction == null) {
            throw NoTransactionException("No current transaction")
        }
        transaction.transaction?.failure()
        transaction.transaction?.close()
        transaction.session?.close()
        transactionCache.set(null)
    }

    /**
     * Unwrap the given Transaction Status object to get the nested Neo4J Transaction
     */
    private fun unwrapTransaction(status: DefaultTransactionStatus) : Neo4jTransaction {
        return status.transaction as Neo4jTransaction
    }
}
