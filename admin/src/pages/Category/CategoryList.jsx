import "../../components/datatable/datatable.scss";
import { DataGrid } from "@mui/x-data-grid";
import { Link, useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";
import CategoryService from "../../services/category.service";
import { Button } from "@mui/material";

const CategoryDatatable = () => {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    loadCategories();
  }, []);

  const loadCategories = async () => {
    try {
      setLoading(true);
      const response = await CategoryService.getAllCategories();
      setCategories(response.data);
    } catch (error) {
      console.error("Error loading categories:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm("Bạn có muốn xóa danh mục này không?")) {
      try {
        await CategoryService.deleteCategory(id);
        setCategories((prevCategories) =>
          prevCategories.filter((category) => category.id !== id)
        );
        alert("Xóa danh mục thành công!");
      } catch (error) {
        console.error("Error deleting category:", error);
        alert("Failed to delete category!");
      }
    }
  };

  const columns = [
    { field: "id", headerName: "ID", width: 100 },
    { field: "title", headerName: "Tên danh mục", width: 200 },
    { field: "description", headerName: "mô tả", width: 300 },
    {
      field: "actions",
      headerName: "Actions",
      width: 200,
      renderCell: (params) => (
        <div className="cellAction">
          <Link
            to={`/categories/${params.row.id}`}
            style={{ textDecoration: "none" }}
          >
            <div className="viewButton">Edit</div>
          </Link>
          <div
            className="deleteButton"
            onClick={() => handleDelete(params.row.id)}
          >
            Disable
          </div>
        </div>
      ),
    },
  ];

  return (
    <div className="datatable">
      <div className="datatableTitle">
        Add New Category
        <Button
          variant="contained"
          color="primary"
          onClick={() => navigate("/categories/new")}
        >
          Add New
        </Button>
      </div>
      <DataGrid
        getRowId={(row) => row.id}
        className="datagrid"
        rows={categories}
        columns={columns}
        loading={loading}
        pageSize={9}
        rowsPerPageOptions={[9]}
        checkboxSelection
      />
    </div>
  );
};

export default CategoryDatatable;
