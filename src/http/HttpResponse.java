package http;

public class HttpResponse {

    String head;
    byte [] payload;

    public HttpResponse(String head, byte [] payload ){
        this.head = head;
        this.payload = payload;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }
}
