import axios from 'axios';
import { SENTIMENT_STRING_ARRAY_URL, SENTIMENT_STRING_URL } from '../../utils';
import { db } from '../../firebase';

const axiosPython = axios.create({
    withCredentials: false,
    headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
    },
});

export const sentimentService = {
    getSentimentByString(text) {
        return axiosPython.get(SENTIMENT_STRING_URL + text)
    },
    getSentimentByStringArray(arr) {
        return axiosPython.post(SENTIMENT_STRING_ARRAY_URL, arr)
    }
}

export const submitReview = async (infoRating, isReview) => {
    try {
        const reviewData = {
            productId: infoRating.productId,
            content: infoRating.content,
            replyForFeedbackId: infoRating.replyForFeedbackId || null,
            star: isReview ? infoRating.star : -1,
            createdAt: new Date(),
        };

        // Tạo document mới trong collection "reviews"
        const reviewRef = await db.collection("reviews").add(reviewData);
        const reviewId = reviewRef.id;

        // Nếu có ảnh, lưu vào Firebase Storage
        if (infoRating.images && infoRating.images.length > 0) {
            const imageUrls = await Promise.all(
                infoRating.images.map(async (image) => {
                    const imageRef = storage.ref(`reviews/${reviewId}/${image.name}`);
                    await imageRef.put(image);
                    return await imageRef.getDownloadURL();
                })
            );

            // Cập nhật đường dẫn ảnh vào Firestore
            await reviewRef.update({ images: imageUrls });
        }

        console.log("Review saved successfully!");
        return true;
    } catch (error) {
        console.error("Error saving review:", error);
        return false;
    }
};
