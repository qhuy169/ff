import { createSlice } from '@reduxjs/toolkit'


const showModalReducers = createSlice({
    name: 'modal',
    initialState:{
        showModal:{
            isShow:false,
            repllyforId: null
        } 
    },
    reducers: {
        setShowModalOn: (state,action)=>{
            state.showModal.isShow = true
            state.showModal.repllyforId = action.payload
        },
        setShowModalOff: (state)=>{
            state.showModal.isShow=false
            state.showModal.repllyforId= null
        }
    }})

    export const {
        setShowModalOff,
        setShowModalOn,
    } = showModalReducers.actions
    
    export default showModalReducers.reducer