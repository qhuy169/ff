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
import { EPayment } from '../../utils';

const useStyles = makeStyles((theme) => ({

    deleteButton: {
        padding: '2px 5px',
        borderRadius: '5px',
        color: 'green',
        cursor: 'pointer'
      },
    deleteButtonGet: {
        color: '#06240647!important',
      }
  }));

function createData(id,fullName, email, phone, address, payment, totalPrice,orderItemsArray) {
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
    address,
    payment,
    totalPrice,
    orderItems
  };
}

function Row(props) {
  const { row } = props;
  const [open, setOpen] = React.useState(false);
  const classes = useStyles();
  const [isClicked, setIsClicked] = useState(false);

  const hanlePostShipment = async(id)=>{
    let res;
    if(isClicked==false){
      res = await shipmentService.postShipment([id]);
      console.log(res);
      
    }
    if(res?.status == "OK"){
      setIsClicked(true);
    }
}
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
        <TableCell align="center">{row.address}</TableCell>
        <TableCell align="center">{EPayment.getNameFromIndex(row.payment)}</TableCell>
        <TableCell align="center">{row.totalPrice}</TableCell>
        <TableCell align="center" className={`${classes.deleteButton} ${isClicked ? classes.deleteButtonGet : ''}`} onClick={(e)=>hanlePostShipment(row.id)}>Get</TableCell>
       
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
    let res = await shipmentService.getAllOrderSameArea();
    let data = res.data.content || []
    data.forEach(item=>{
        rows.push(createData(
            item?.id,
            item?.fullName
            ,item?.email
            ,item?.phone
            ,`${item?.address.homeAdd}, ${item?.address.ward}, ${item?.address.district}, ${item?.address.city}`
            ,item?.payment
            ,item?.totalPrice
            ,item?.orderItems))
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
            <TableCell align="center">Address</TableCell>
            <TableCell align="center">Payment</TableCell>
            <TableCell align="center">Price</TableCell>
            <TableCell align="center">Action</TableCell>
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