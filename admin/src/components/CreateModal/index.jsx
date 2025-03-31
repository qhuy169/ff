import { Modal, TextareaAutosize, TextField } from "@material-ui/core";
import { useDispatch, useSelector } from "react-redux";
import { useEffect, useRef, useState } from "react";
import useStyles from './styles'
import {Button} from '@material-ui/core'
import { useCallback } from "react";
import {showModal,offModal} from '../../redux/modal/modalApi';
import {getAllCommentsfull} from '../../redux/comment/commentApi'
import { useParams } from "react-router-dom";
import moment from 'moment'
import {repplyComments} from '../../redux/comment/commentApi'
function CreatePostModal() {
    const dispatch = useDispatch()
    const more = useRef()
    const idPro = useParams()
    const [data,setData] = useState({
      content: '',
      sdt: '',
      name:'',
      gmail:''
    })
    let isShow = useSelector(state=>state.modal.showModal.isShow);
    let repllyforId = useSelector(state=>state.modal.showModal.repllyforId);
    useEffect(()=>{
      getAllCommentsfull(dispatch)
    },[])

    let getComment = useSelector((state) => state.comments.comment.data);
    const onSubmit=(e)=>{
        e.preventDefault()
        repplyComments(dispatch,idPro.commentId,{ 
            id: getComment[getComment.length-1]?.id + 1,
            content: data.content,
            create_date: moment().format('HH:MM MM/DD, YYYY'),
            creator: {
                id: parseInt(data.sdt),
                name: data.name,
                avatar: 'https://cafedev.vn/wp-content/uploads/2019/12/cafedev_javascript.png',
                username: data.gmail,
                admin: true,
                replyforId: repllyforId,
            },
            productId: idPro.commentId,
          })
          console.log({ 
            id: getComment[getComment.length-1]?.id + 1,
            content: data.content,
            create_date: moment().format('HH:MM MM/DD, YYYY'),
            creator: {
                id: parseInt(data.sdt),
                name: data.name,
                avatar: 'https://cafedev.vn/wp-content/uploads/2019/12/cafedev_javascript.png',
                username: data.gmail,
                admin: true,
                replyforId: repllyforId,
            },
            productId: idPro.commentId,
          })
        onClose()
        setData({
          content: '',
          sdt: '',
          name:'',
          gmail:''
      })
    }
    const onClose = useCallback(()=>{
        offModal(dispatch)
    },[dispatch])
     // Đóng khi click ra ngoài
     const useOutsideAlerter = (ref) => {
      useEffect(() => {
          const handleClickOutside = (event) => {
              if (ref.current && !ref.current.contains(event.target)) {
                  offModal(dispatch)
              }
          };
          // Bind the event listener
          document.addEventListener('mousedown', handleClickOutside);
          return () => {
              // Unbind the event listener on clean up
              document.removeEventListener('mousedown', handleClickOutside);
          };
      });
  };
  useOutsideAlerter(more)
    const classes = useStyles()
    const body=(
        <div className={classes.paper} id='simple-modal-title' ref={more}>
          <h2>Replly Comment</h2>
          <form noValidate autoComplete='off' className={classes.form}>
            <TextField
              className={classes.title}
              required
              label='Họ tên'
              value={data.title}
              onChange={(e) => setData({ ...data, name: e.target.value })}
            />
              <TextField
              className={classes.title}
              required
              label='Sdt'
              value={data.title}
              onChange={(e) => setData({ ...data, sdt: e.target.value })}
            />
              <TextField
              className={classes.title}
              required
              label='gmail'
              value={data.title}
              onChange={(e) => setData({ ...data, gmail: e.target.value })}
            />
            <TextareaAutosize
              className={classes.textarea}
              minRows={10}
              maxRows={15}
              placeholder='Content...'
              value={data.content}
              onChange={(e) => setData({ ...data, content: e.target.value })}
            />
         
            <div className={classes.footer}>
              <Button
                variant='contained'
                color='primary'
                component='span'
                fullWidth
                onClick={onSubmit}
              >
                 Repply 
              </Button>
            </div>
          </form>
        </div>
      );
    return ( <Modal open={isShow} onClose={onClose}>
        {body}
    </Modal>);
}

export default CreatePostModal;