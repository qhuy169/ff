// import Datatable from "./Datatable"

// const Shipment =()=>{
//     return (
//         <Datatable/>
//   )
// }
// export default Shipment;

import * as React from 'react';
import PropTypes from 'prop-types';
import Box from '@mui/material/Box';
import Collapse from '@mui/material/Collapse';
import IconButton from '@mui/material/IconButton';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Typography from '@mui/material/Typography';
import Paper from '@mui/material/Paper';
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import KeyboardArrowUpIcon from '@mui/icons-material/KeyboardArrowUp';
import { Link } from 'react-router-dom';
import { makeStyles } from '@material-ui/core/styles';
import { shipmentService } from '../../services/shipment';
import { useEffect } from 'react';
import { useState } from 'react';
import { Button } from '@mui/material';
import { EPayment } from '../../utils';

const useStyles = makeStyles((theme) => ({

    deleteButton: {
        padding: '2px 5px',
        borderRadius: '5px',
        color: 'green',
        cursor: 'pointer'
      }
  }));

function createData(id,fullName, email, phone, addressFrom,addressTo, payment, totalPrice,orderItemsArray) {
  let orderItems = [];
  orderItemsArray.forEach(item=>{
    orderItems.push( {
        title: item?.product?.title,
        price: item?.product?.price,
        quantity: item?.quantity,
        totalPrice: item?.totalPrice,
      },
    )
  })
  return {
    id,
    fullName,
    email,
    phone,
    addressFrom,
    addressTo,
    payment,
    totalPrice,
    orderItems
  };
}

function Row(props) {
  const { row } = props;
  const [open, setOpen] = React.useState(false);
  const [isClicked, setIsClicked] = useState(false);
  const hanlePostShipment = async(id)=>{
    let res;
    if(isClicked==false){
      res = await shipmentService.putShipment(id);
    }
    if(res?.status == "OK"){
      setIsClicked(true);
    }
  }

  const classes = useStyles();
  return (
    <React.Fragment>
      <TableRow sx={{ '& > *': { borderBottom: 'unset' } }}>
        <TableCell>
          <IconButton
            aria-label="expand row"
            size="medium"
            onClick={() => setOpen(!open)}
          >
            {open ? <KeyboardArrowUpIcon /> : <KeyboardArrowDownIcon />}
          </IconButton>
        </TableCell>
        <TableCell component="th" scope="row">
          {row.fullName}
        </TableCell>
        <TableCell align="center">{row.email}</TableCell>
        <TableCell align="center">{row.phone}</TableCell>
        <TableCell align="center">{row.addressFrom}</TableCell>
        <TableCell align="center">{row.addressTo}</TableCell>
        <TableCell align="center">{EPayment.getNameFromIndex(row.payment)}</TableCell>
        <TableCell align="center">{row.totalPrice}</TableCell>
      </TableRow>
      <TableRow>
        <TableCell style={{ paddingBottom: 0, paddingTop: 0 }} colSpan={12}>
          <Collapse in={open} timeout="auto" unmountOnExit>
            <Box sx={{ margin: 1 }}>
              <Typography variant="h6" gutterBottom component="div">
                Order Items
              </Typography>
           
              <Table size="medium" aria-label="purchases">
                <TableHead>
                  <TableRow>
                    <TableCell>Name</TableCell>
                    <TableCell>Price</TableCell>
                    <TableCell>Quantity</TableCell>
                    <TableCell align="right">Total price ($)</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {row.orderItems.map((item) => (
                    <TableRow key={item.title}>
                      <TableCell component="th" scope="row">
                        {item.title}
                      </TableCell>
                      <TableCell>{item.price}</TableCell>
                      <TableCell>{item.quantity}</TableCell>
                      <TableCell align="right">{item.totalPrice}</TableCell>
                
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
              <Typography onClick={e=>hanlePostShipment(row.id)} className={` float-right p-4 border rounded-md mt-4 ${isClicked ? 'text-gray-200' : 'text-red-400'}`} variant="button" gutterBottom component="button">
                Xác nhận thành công
              </Typography>
            </Box>
          </Collapse>
        </TableCell>
      </TableRow>
    </React.Fragment>
  );
}

// Row.propTypes = {
//   row: PropTypes.shape({
//     calories: PropTypes.number.isRequired,
//     carbs: PropTypes.number.isRequired,
//     fat: PropTypes.number.isRequired,
//     history: PropTypes.arrayOf(
//       PropTypes.shape({
//         amount: PropTypes.number.isRequired,
//         customerId: PropTypes.string.isRequired,
//         date: PropTypes.string.isRequired,
//       }),
//     ).isRequired,
//     name: PropTypes.string.isRequired,
//     price: PropTypes.number.isRequired,
//     protein: PropTypes.number.isRequired,
//   }).isRequired,
// };


export default function Shipment() {
  let rows = [];
  const [dataget,setDataget] = useState([]);
  const getData = async()=>{
    let res = await shipmentService.getAllOrderShipment();
    let data = res.data.content || []
  console.log("koko",res)

    data.forEach(item=>{
        rows.push(createData(
            item?.id,
            item?.orderShop?.fullName
            ,item?.orderShop?.email
            ,item?.orderShop?.phone
            ,`${item?.fromAddress.homeAdd}, ${item?.fromAddress.ward}, ${item?.fromAddress.district}, ${item?.fromAddress.city}`
            ,`${item?.toAddress.homeAdd}, ${item?.toAddress.ward}, ${item?.toAddress.district}, ${item?.toAddress.city}`
            ,item?.orderShop?.payment
            ,item?.orderShop?.totalPrice
            ,item?.orderShop?.orderItems))
    });
    setDataget(rows)
  }
  useEffect(()=>{
    getData()

  },[])
  return (
    <TableContainer component={Paper}>
      <Table aria-label="collapsible table">
        <TableHead>
          <TableRow>
            <TableCell />
            <TableCell>Customer</TableCell>
            <TableCell align="center">Email</TableCell>
            <TableCell align="center">Phone</TableCell>
            <TableCell align="center">Pick Up Address</TableCell>
            <TableCell align="center">Delivery Address</TableCell>
            <TableCell align="center">Payment</TableCell>
            <TableCell align="center">Price</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {dataget.map((row) => (
            <Row key={row.name} row={row} />
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
}