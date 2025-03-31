import * as React from 'react';
import Box from '@mui/material/Box';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import Divider from '@mui/material/Divider';
import InboxIcon from '@mui/icons-material/Inbox';
import DraftsIcon from '@mui/icons-material/Drafts';
import './HistoryEmail.scss'
import { useState } from 'react';

import ListItemAvatar from '@mui/material/ListItemAvatar';
import Avatar from '@mui/material/Avatar';
import Typography from '@mui/material/Typography';
import { getAllEmailByShopId } from '../../../redux/Email/EmailApi';
import { useDispatch, useSelector } from 'react-redux';
import { useEffect } from 'react';
import { API_IMAGE_URL } from '../../../utils';
import { Link } from 'react-router-dom';
import { OAUTH2_REDIRECT_URI } from '../../../utils';
const HistoryEmail = () => {
  const [inbox, setInbox] = useState(true)
  const [emailDetail, setEmailDetail] = useState(null)
  const handleDetailMessage = (item) => {
    setInbox(false);
    setEmailDetail(item)
  }
  const dispatch = useDispatch()

  const messagesList = useSelector((state) => state.emails.allEmail.data)
  const getUser = JSON.parse(localStorage.getItem('customerInfo'));
  console.log(getUser);

  useEffect(() => {
    getAllEmailByShopId(dispatch, getUser?.shopId)
  }, [])

  const hanleProductImages = (jsonString) => {
    const cleanedJsonString = jsonString.replace(/,}/g, '}');
    const jsonData = JSON.parse(cleanedJsonString);
    return jsonData
  }
  const createMarkup = () => {
    return { __html: emailDetail?.body };
  };
  console.log('email', messagesList)
  return (
    <div className='m-6 flex gap-5 h-[700px] mb-0'>
      <Box sx={{ minWidth: '16%', maxWidth: 360, bgcolor: 'background.paper' }} >
        <nav aria-label="main mailbox folders">
          <List>
            <ListItem disablePadding>
              <ListItemButton onClick={e => { setInbox(true); setEmailDetail(null) }}>
                <ListItemIcon >
                  <InboxIcon sx={{ fontSize: '18px' }} />
                </ListItemIcon>
                <ListItemText primary="Inbox"
                  primaryTypographyProps={{ sx: { fontSize: '13px' } }}
                />
              </ListItemButton>
            </ListItem>
            <ListItem disablePadding>
              <ListItemButton>
                <ListItemIcon>
                  <DraftsIcon sx={{ fontSize: '18px' }} />
                </ListItemIcon>
                <ListItemText primary="Drafts"
                  primaryTypographyProps={{ sx: { fontSize: '13px' } }}
                />
              </ListItemButton>
            </ListItem>
          </List>
        </nav>
        <Divider />
        <nav aria-label="secondary mailbox folders">
          <List>
            <ListItem disablePadding>
              <ListItemButton>
                <ListItemText primary="Trash"
                  primaryTypographyProps={{ sx: { fontSize: '12px' } }}
                />
              </ListItemButton>
            </ListItem>
            <ListItem disablePadding>
              <ListItemButton component="a" href="#simple-list">
                <ListItemText primary="Spam"
                  primaryTypographyProps={{ sx: { fontSize: '12px' } }}
                />
              </ListItemButton>
            </ListItem>
          </List>
        </nav>
      </Box>
      {inbox == true && emailDetail == null ?
        <List sx={{ width: '100%', bgcolor: 'background.paper' }} className="flex-1">

          {messagesList.map(item => <>
            <ListItem alignItems="flex-start" sx={{ cursor: 'pointer' }} onClick={e => handleDetailMessage(item)}>
              <ListItemAvatar  >
                <Avatar alt="Remy Sharp" src="/static/images/avatar/1.jpg" />
              </ListItemAvatar>
              <ListItemText
                primary={item.title}
                primaryTypographyProps={{ sx: { fontSize: '15px' } }}
                secondary={
                  <React.Fragment>
                    <Typography
                      sx={{ display: 'inline', fontSize: '11px' }}
                      component="span"
                      variant="body2"
                      color="text.primary"
                    >
                      {item.toName}
                    </Typography>
                    <Typography
                      sx={{ display: 'inline', fontSize: '13px' }} // Increase the font size here
                      component="span"
                      variant="body2"
                    >
                      {" — We are sending you this letter to let you know that…"}
                    </Typography>
                  </React.Fragment>
                }
              />
            </ListItem>
            <Divider variant="inset" component="li" />
          </>)}

        </List> : <div className="bg-white p-12">
          <div className="card-body flex-col flex-1">
            <div className="flex gap-4 mb-5">
              <div className="h-[60px] w-[60px]">
                <img className="w-full h-full rounded-full" src="https://www.clipartmax.com/png/middle/257-2572603_user-man-social-avatar-profile-icon-man-avatar-in-circle.png" alt="Avatar" />
              </div>
              <div className="mb-5">
                <div className="flex-1 align-self-center">
                  <h4 className="font-bold m-0">PXC Admin</h4>
                  <small className="text-muted">support@pntech.com</small>
                </div>
              </div>
            </div>

            <h4 className="mt-6 text-2xl leading-10 font-semibold ">{emailDetail?.title}</h4>
            <div dangerouslySetInnerHTML={createMarkup()} />
            <hr />

            <div className="mt-6 flex gap-2 flex-1">
              {emailDetail?.products && hanleProductImages(emailDetail?.products)?.map(item => (
                <div className="p-4 w-[290px]">
                  <div className="text-xl font-semibold mb-2">{item.name.length < 40 ? item.name : `${item.name.slice(0, 36)}...`}</div>
                  <span className="text-[12px] mb-6">Notify: {item.count}</span>
                  <div className="h-[160px] w-[250px] my-2">
                    <img className="w-full h-full" src={`${API_IMAGE_URL}${item?.img}`} alt="Product" />
                  </div>
                  <Link to={`${OAUTH2_REDIRECT_URI}/${item.slug}`} className="text-xl font-semibold mb-2 text-red-400 flex">Preview: {OAUTH2_REDIRECT_URI}/{item.slug.slice(0, 7)}...</Link>
                </div>
              ))}
            </div>
          </div>
        </div>
      }
    </div>

  );
}

export default HistoryEmail;
