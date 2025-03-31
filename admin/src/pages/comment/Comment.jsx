import { useState, useEffect } from "react";
import Sidebar from "~/components/sidebar/Sidebar";
import Navbar from "~/components/navbar/Navbar";
import Datatable from "~/components/datatable/Datatable";
import { ProductService } from "~/services";
import { productColumns } from "~/datatablesource";

const Comment = () => {
    const [data, setData] = useState([]);

    useEffect(() => {
        // Fetch products when component mounts
        function getProducts() {
            ProductService.getProducts(1, 9) // Fetch products (1st page, 9 items per page)
                .then((res) => {
                    // Check the structure of the response
                    console.log(res.data);
                    setData(res.data); // Set the fetched products in state
                })
                .catch((error) => {
                    console.error("Error fetching products:", error);
                });
        }
        getProducts();
    }, []);

    // Call the productColumns function to get the column definitions
    const columns = productColumns();

    return (
        <div>
            <Sidebar />
            <Navbar />
            <Datatable
                rows={data}               // Pass the data fetched from API
                title="Product List"      // Set the title for the Datatable
                productColumns={columns}  // Pass the correct columns to the Datatable
                type="comment"            // You can change 'comment' based on use case
            />
        </div>
    );
};

export default Comment;
