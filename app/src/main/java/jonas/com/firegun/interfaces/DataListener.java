package jonas.com.firegun.interfaces;

public interface DataListener<T> {
    void onError(String msg);

    void onDataUpdate(T response);
}
