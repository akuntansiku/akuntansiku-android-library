package id.co.akuntansiku.utils.retrofit.model;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class ApiResponse {
    Object data;
    String status;
    Meta meta;

    public JSONObject getData() throws JSONException {
        Gson gson = new Gson();
        String json = gson.toJson(data);
        JSONObject object = new JSONObject(json);
        return object;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public class Meta {
        int code;
        String error_message;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getError_message() {
            return error_message;
        }

        public void setError_message(String error_message) {
            this.error_message = error_message;
        }
    }
}


