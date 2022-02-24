import { requireNativeComponent } from 'react-native';
import { AnythinkModuleBridge } from './module';

const AnythinkView = requireNativeComponent('AnythinkView');
export default AnythinkView;
export { AnythinkModuleBridge };
