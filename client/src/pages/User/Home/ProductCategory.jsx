import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import Section from './Section';
import styles from './productcategory.module.scss';
import { useDispatch, useSelector } from 'react-redux';
import { getAllCategoriesApi } from '../../../redux/category/categoriesApi';

function ProductCategory() {
    const dispatch = useDispatch();

    const categories = useSelector((state) => state.categories?.allCategory?.data)||[];

    useEffect(() => {
        getAllCategoriesApi(dispatch);
    }, []);

    // console.log(categories);

    return (
        <Section title="DANH MỤC NỔI BẬT" styles="bg-white">
            <div className={styles.body}>
                {categories?.map((item) => (
                    <Link to={item.href} key={item.title}>
                        <div className={styles.cateIcon}>
                            <img src={item.img} alt="" className={styles.image} />
                            <p className={styles.title}>{item.title}</p>
                        </div>
                    </Link>
                ))}
            </div>
        </Section>
    );
}

export default ProductCategory;
