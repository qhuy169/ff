import{setShowModalOff,setShowModalOn} from './showModalSlice'
export const showModal = async(dispatch,repllyforId)=>{
   
    try {
      dispatch(setShowModalOn(repllyforId))  
     
    } catch (error) {
        console.log(error)
        console.log('OK')
    }
}
export const offModal= async(dispatch)=>{
    try {
        dispatch(setShowModalOff())
    } catch (error) {
        console.log(error)
    }
}