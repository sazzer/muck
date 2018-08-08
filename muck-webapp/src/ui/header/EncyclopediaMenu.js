// @flow

import React from 'react';
import {Dropdown, Input} from 'semantic-ui-react';
import {Interpolate, I18n} from 'react-i18next';
import {Link} from 'react-router-dom';

/**
 * The header bar menu for the game encyclopedia
 */
export default function EncyclopediaMenu() {
    return (
        <I18n>
            {
                (t) => (
                    <Dropdown item text={t('page.header.encyclopedia.menu')}>
                        <Dropdown.Menu>
                            <Link to='/encyclopedia/races'>
                                <Dropdown.Item>
                                    <Interpolate i18nKey='page.header.encyclopedia.categories.races' />
                                </Dropdown.Item>
                            </Link>
                            <Link to='/encyclopedia/abilities'>
                                <Dropdown.Item>
                                    <Interpolate i18nKey='page.header.encyclopedia.categories.abilities' />
                                </Dropdown.Item>
                            </Link>
                            <Link to='/encyclopedia/skills'>
                                <Dropdown.Item>
                                    <Interpolate i18nKey='page.header.encyclopedia.categories.skills' />
                                </Dropdown.Item>
                            </Link>
                            <Link to='/encyclopedia/feats'>
                                <Dropdown.Item>
                                    <Interpolate i18nKey='page.header.encyclopedia.categories.feats' />
                                </Dropdown.Item>
                            </Link>
                            <Dropdown.Divider />
                            <Dropdown.Item>
                                <form onClick={(e) => e.stopPropagation()}>
                                    <Input icon='search' iconPosition='left' className='search' focus />
                                </form>
                            </Dropdown.Item>
                        </Dropdown.Menu>
                    </Dropdown>
                )
            }
        </I18n>
    );
}
