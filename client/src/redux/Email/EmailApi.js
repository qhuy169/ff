import { emailService } from '../../services';
import { AllEmails,EmailDetail} from './EmailSlice'

export const getAllEmailByShopId = async (dispatch,id) => {
    let res = await emailService.getAllEmailsByShopId(id);
    console.log("res",res)
    dispatch(AllEmails(res.data));
};
export const getEmailDetail= async (dispatch, id) => {
    if(id == null){
        let resfirst = await emailService.getAllEmails();
        let res = await emailService.getEmail(resfirst?.result[0]?.emailID);
        dispatch(EmailDetail(res));
    }else{
        let res = await emailService.getEmail(id);
        console.log(res);
        dispatch(EmailDetail(res));
    }
};

export const postEmail = async (dispatch,email) => {
    
    if(email.timetosend != ""){
        console.log(email)
        await emailService.postEmailSchedule(email);
        let resAllMails = await emailService.getAllEmails();
        dispatch(AllEmails(resAllMails));
    }else{
        await emailService.postEmail(email);
        let resAllMails = await emailService.getAllEmails();
        dispatch(AllEmails(resAllMails));
    }
};

export const deleteEmail = async (dispatch,id) => {
    let res = await emailService.deleteEmail(id);
    let resAllMails = await emailService.getAllEmails();
    dispatch(AllEmails(resAllMails));
    //dispatch(getLocationProduct(res));
};

