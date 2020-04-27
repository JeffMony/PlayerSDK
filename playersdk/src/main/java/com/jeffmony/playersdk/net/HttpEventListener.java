package com.jeffmony.playersdk.net;

import androidx.annotation.Nullable;

import com.jeffmony.playersdk.LogUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;

import okhttp3.Call;
import okhttp3.Connection;
import okhttp3.EventListener;
import okhttp3.Handshake;
import okhttp3.HttpUrl;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

public class HttpEventListener extends EventListener {

    private static final String TAG = "HttpEventListener";
    /**
     * EventListener.Factory: listener for the http request.
     */
    public static final Factory FACTORY = new Factory() {
        final AtomicLong nextCallId = new AtomicLong(1L);

        @Override
        public EventListener create(Call call) {
            long callId = nextCallId.getAndIncrement();
            return new HttpEventListener(callId, call.request().url(),
                    System.nanoTime());
        }
    };

    /**
     * request id
     */
    private final long callId;

    /**
     * request start
     */
    private final long callStartNanos;

    private StringBuilder sbLog;

    public HttpEventListener(long callId, HttpUrl url, long callStartNanos) {
        this.callId = callId;
        this.callStartNanos = callStartNanos;
        this.sbLog = new StringBuilder(url.toString())
                .append(",Id=")
                .append(callId)
                .append(":\n");
    }

    private void recordEventLog(String name) {
        long elapseNanos = System.nanoTime() - callStartNanos;
        sbLog
                .append(String.format(Locale.CHINA, "%.3f-%s",
                        elapseNanos / 1000000000d, name))
                .append(";\n");
        if (name.equalsIgnoreCase("callEnd") ||
                name.equalsIgnoreCase("callFailed")) {
            // print http pipeline info.
            LogUtils.w(sbLog.toString());
        }
    }

    @Override
    public void callStart(Call call) {
        super.callStart(call);
        recordEventLog("callStart");
    }

    @Override
    public void dnsStart(Call call, String domainName) {
        super.dnsStart(call, domainName);
        recordEventLog("dnsStart");
    }

    @Override
    public void dnsEnd(Call call, String domainName,
                       List<InetAddress> inetAddressList) {
        super.dnsEnd(call, domainName, inetAddressList);
        recordEventLog("dnsEnd");
    }

    @Override
    public void connectStart(Call call, InetSocketAddress inetSocketAddress,
                             Proxy proxy) {
        super.connectStart(call, inetSocketAddress, proxy);
        recordEventLog("connectStart");
    }

    @Override
    public void secureConnectStart(Call call) {
        super.secureConnectStart(call);
        recordEventLog("secureConnectStart");
    }

    @Override
    public void secureConnectEnd(Call call, @Nullable Handshake handshake) {
        super.secureConnectEnd(call, handshake);
        recordEventLog("secureConnectEnd");
    }

    @Override
    public void connectEnd(Call call, InetSocketAddress inetSocketAddress,
                           Proxy proxy, @Nullable Protocol protocol) {
        super.connectEnd(call, inetSocketAddress, proxy, protocol);
        recordEventLog("connectEnd");
    }

    @Override
    public void connectFailed(Call call, InetSocketAddress inetSocketAddress,
                              Proxy proxy, @Nullable Protocol protocol,
                              IOException ioe) {
        super.connectFailed(call, inetSocketAddress, proxy, protocol, ioe);
        recordEventLog("connectFailed");
    }

    @Override
    public void connectionAcquired(Call call, Connection connection) {
        super.connectionAcquired(call, connection);
        recordEventLog("connectionAcquired");
    }

    @Override
    public void connectionReleased(Call call, Connection connection) {
        super.connectionReleased(call, connection);
        recordEventLog("connectionReleased");
    }

    @Override
    public void requestHeadersStart(Call call) {
        super.requestHeadersStart(call);
        recordEventLog("requestHeadersStart");
    }

    @Override
    public void requestHeadersEnd(Call call, Request request) {
        super.requestHeadersEnd(call, request);
        recordEventLog("requestHeadersEnd");
    }

    @Override
    public void requestBodyStart(Call call) {
        super.requestBodyStart(call);
        recordEventLog("requestBodyStart");
    }

    @Override
    public void requestBodyEnd(Call call, long byteCount) {
        super.requestBodyEnd(call, byteCount);
        recordEventLog("requestBodyEnd");
    }

    @Override
    public void responseHeadersStart(Call call) {
        super.responseHeadersStart(call);
        recordEventLog("responseHeadersStart");
    }

    @Override
    public void responseHeadersEnd(Call call, Response response) {
        super.responseHeadersEnd(call, response);
        recordEventLog("responseHeadersEnd");
    }

    @Override
    public void responseBodyStart(Call call) {
        super.responseBodyStart(call);
        recordEventLog("responseBodyStart");
    }

    @Override
    public void responseBodyEnd(Call call, long byteCount) {
        super.responseBodyEnd(call, byteCount);
        recordEventLog("responseBodyEnd");
    }

    @Override
    public void callEnd(Call call) {
        super.callEnd(call);
        recordEventLog("callEnd");
    }

    @Override
    public void callFailed(Call call, IOException ioe) {
        super.callFailed(call, ioe);
        recordEventLog("callFailed");
    }
}
