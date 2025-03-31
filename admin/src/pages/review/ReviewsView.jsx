import { useState, useEffect } from "react";
import Sidebar from "~/components/sidebar/Sidebar";
import Navbar from "~/components/navbar/Navbar";
import Datatable from "~/components/datatable/Datatable";
import { useParams } from "react-router-dom";
import { ProductService } from "~/services";
import { commentColumns } from "~/datatablesource";
import { useDispatch, useSelector } from "react-redux";
import { getAllReviews } from "../../redux/review/reviewApi";
import { reviewDetailColumns } from "../../datatablesource";
const ReviewsView = () => {
    const { reviewId } = useParams();
    const dispatch = useDispatch();
    console.log(reviewId);
    useEffect(() => {
        getAllReviews(dispatch, reviewId);
    }, []);
    let getReview = useSelector((state) => state.reviews.review.data);

    return (
        <div>
            <div>
                <Datatable
                    rows={getReview}
                    title=""
                    productColumns={reviewDetailColumns}
                    type="review"
                />
            </div>
        </div>
    );
};

export default ReviewsView;
