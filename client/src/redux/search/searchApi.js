import { searchService } from '../../services/search.service';
import { getResultSearch, removeResultSearch } from './searchSlice';
export const getResult = async (dispatch, parameter) => {
    let res = await searchService.getResultSearchApi(parameter);
    dispatch(getResultSearch(res.data));
};
export const removeResult = (dispatch) => {
    dispatch(removeResultSearch());
};
