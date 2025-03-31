import './buttonquicklink-module.scss';

const QuickLink = (props) => {
    return (
        <a onClick={() => props.handleSetChose(props?.id || props.type)}>
            {props.demand}
            {props.link && <img id={props.type} src={props.link} alt={props.type}/> ||
            <p htmlFor={props.type}>{props.type}</p> }
        </a>
    );
};
export default QuickLink;
