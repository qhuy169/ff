import "./list.scss"
import { useState, useEffect } from 'react'
import Sidebar from "~/components/sidebar/Sidebar"
import Navbar from "~/components/navbar/Navbar"
import Datatable from "~/components/datatable/Datatable"
import { useLocation } from 'react-router-dom';
import { ProductService } from '~/services'
import { productColumns } from "~/datatablesource";

const ListReview = () => {
  const locationUrl = useLocation();  
	console.log(locationUrl.pathname)
	const [data, setData] = useState([])

	useEffect(() => {
		function getProducts() {
			ProductService.getProducts(1, 9)
				.then(res => setData(res.data))
		}
		getProducts()
	}, [])
  return (
    <div className="list">
      <Sidebar/>
      <div className="listContainer">
        <Navbar/>
        <Datatable rows={data} title="" productColumns={productColumns} />
      </div>
    </div>
  )
}

export default ListReview
