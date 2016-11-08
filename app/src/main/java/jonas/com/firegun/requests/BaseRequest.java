package jonas.com.firegun.requests;


import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;

import jonas.com.firegun.interfaces.DataListener;

/**
 * This is the generic request. All request must extend this class
 * @param <T> the response.
 */
public class BaseRequest<T> extends Request<T> {

    private static final int MAX_RETRIES = 2;
    private static final int TIMEOUT = 10000;

    private final DataListener<T> dataListener;

    private final Gson gson = new Gson();
    private final Class<T> clazz;

    BaseRequest(int method, String url, Class<T> clazz, DataListener<T> dataListener) {
        super(method, url, null);

        Log.d("Request-Url", url);

        // Retry policy and timeout
        setRetryPolicy(new DefaultRetryPolicy(TIMEOUT,
                MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //Tag to identify the event in case you need to cancel it.
        setTag(clazz.getSimpleName());

        this.clazz = clazz;
        this.dataListener = dataListener;
    }

    @Override
    protected void deliverResponse(T response) {
        dataListener.onDataUpdate(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        dataListener.onError(error.getMessage());
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            Log.d("Request-Response", json);
            return Response.success(gson.fromJson(json, clazz),HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}