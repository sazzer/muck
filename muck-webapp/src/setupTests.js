import {configure} from 'enzyme';
import Adapter from 'enzyme-adapter-react-16';
import 'jest-enzyme';

configure({ adapter: new Adapter() });

window.ConfigurationOptions = {
    API_URI: 'http://example.com:8080/api'
};
