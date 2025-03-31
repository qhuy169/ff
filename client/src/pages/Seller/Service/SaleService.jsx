import React, { useEffect, useState } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { Card, CardHeader, CardMedia, CardContent, Typography, Button } from '@material-ui/core';
import { shopService } from '../../../services';
import Slider from 'react-slick';
import NextArrow from '~/components/Slick/NextArrow';
import PrevArrow from '~/components/Slick/PrevArrow';
import 'slick-carousel/slick/slick.css';
import 'slick-carousel/slick/slick-theme.css';
import { paypal } from '../../../services/payment';

const useStyles = makeStyles((theme) => ({
  card: {
    maxWidth: 400,
    margin: 'auto',
    boxShadow: '0 2px 6px rgba(0, 0, 0, 0.15)',
    borderRadius: theme.spacing(2),
    overflow: 'hidden',
    zIndex: 2,
    transition: 'transform 0.2s',
    '&:hover': {
      transform: 'scale(1.02)',
    },
  },
  media: {
    height: 0,
    paddingTop: '56.25%', // 16:9 aspect ratio
  },
  content: {
    padding: theme.spacing(2),
  },
  description: {
    marginBottom: theme.spacing(2),
    fontSize: 14,
    color: '#333'
  },
  fee: {
    fontWeight: 'bold',
    color: '#e7158d',

  },
  button: {
    marginTop: theme.spacing(1.5),
    textAlign: 'center',
    backgroundColor:'#94c292',
  },
  cardHeader: {
    backgroundColor: '#f5f5f5',
    borderBottom: '1px solid #ddd',
    padding: theme.spacing(2),
  },
  cardHeaderText: {
    fontWeight: 'bold',
    fontSize: '1.7rem',
    color:'#db1242',
    textAlign: 'center'
  },
  time: {
    fontSize: 14,
    marginBottom: theme.spacing(1),
    color: '#333895fa',
  },
  customSlider: {
    width: '960px',
    '& .slick-next':{
        right: '-13px!important',
        top: '24%!important'
    },
    '& .slick-prev':{
        top: '24%!important',
        left: '-35px!important'
    },
    '& .slick-slide': {
      margin: `0 5px`, // Adjust the margin value as needed
    },
  },
}));

const SaleService = ({ title, time, description, image, fee }) => {
  const classes = useStyles();
  const [data,setData]= useState([])
  const getUser = JSON.parse(localStorage.getItem('customerInfo'));
  const getData = async()=>{
    let res = await shopService.getPricesPackage();
    setData(res.data)
  }
  const postPricePackage = async(idPrice)=>{
    const body ={
        "shopId": getUser?.shopId,
        "fullName": getUser?.fullName,
        "shopPriceId": idPrice,
        "redirectUrl": "http://localhost:5000/#/Seller/"
    }
    let resVNPay = await paypal.postPricePackage(body)
    window.location = resVNPay.data.payUrl;
   
  }
  useEffect(()=>{
    getData()
  },[])
  const handlePurchase = async(idPrice) => {
    // Handle purchase logic here
    postPricePackage(idPrice)
  };

  return (
    <div className='w-full flex justify-center mt-[5%]'>
    <Slider slidesToShow={2} dots={true} slidesToScroll={2}  nextArrow={<NextArrow />} prevArrow={<PrevArrow />} autoplay={true} autoplaySpeed={2000} className={classes.customSlider}  >
         {data?.map(item=>
    <Card className={classes.card}>
      <CardHeader className={classes.cardHeader} title={item?.name}  classes={{
          title: classes.cardHeaderText,
        }}/>
     
      <CardMedia className={classes.media} image={"https://cdn-icons-png.flaticon.com/512/2867/2867644.png"} title={item?.name} />
      <CardContent className={classes.content}>
        <Typography variant="body2" color="textSecondary" className={classes.description}>
          {item?.description}
        </Typography>
        <Typography variant="body2" className={classes.time}>
          Time: {item?.time}
        </Typography>
        <Typography variant="h6" component="p" className={classes.fee}>
          Fee: {item?.price}
        </Typography>
        <Button
          className={classes.button}
          variant="contained"
          color="primary"
          onClick={e=>handlePurchase(item.id)}
        >
          Purchase
        </Button>
      </CardContent>
    </Card> 
    )}
  </Slider>
  </div>
  );
};

export default SaleService;
