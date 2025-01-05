package product.service.customeException;



public class GlobelExceptioHandle extends RuntimeException{

    private String msg;
    private int code;


    public GlobelExceptioHandle(String msg) {
        super(msg);
    }

    public GlobelExceptioHandle(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
