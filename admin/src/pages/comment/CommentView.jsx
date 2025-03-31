import { useState, useEffect } from "react";
import Sidebar from "~/components/sidebar/Sidebar";
import Navbar from "~/components/navbar/Navbar";
import Datatable from "~/components/datatable/Datatable";
import { useLocation, useParams } from "react-router-dom";
import { ProductService } from "~/services";
import { commentColumns } from "~/datatablesource";
import { getAllComments } from "../../redux/comment/commentApi";
import { useDispatch, useSelector } from "react-redux";
const CommentView = () => {
    const { commentId } = useParams();
    const dispatch = useDispatch();
    const [product,setProduct] = useState([])
    console.log(commentId);
    useEffect(() => {
        getAllComments(dispatch, commentId);
        ProductService.getProduct(commentId).then((res) =>
            setProduct(res.data)
        );
    }, []);
    let getComment = useSelector((state) => state.comments.comment.data);

    console.log(getComment);
    return (
        <div>
            <div>
                <Datatable
                    rows={getComment}
                    title=""
                    productColumns={commentColumns}
                    type="comment"
                    reply={true}
                />
            </div>
        </div>
    );
};

export default CommentView;
