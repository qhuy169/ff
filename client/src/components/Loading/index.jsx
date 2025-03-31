import Spinner from '~/components/Spinner';
function Loading() {
    return (
        <div className="h-screen bg-while">
            <Spinner size={60} />
        </div>
    );
}

export default Loading;
