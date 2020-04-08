package com.android.jesse.log.logger;

import android.text.TextUtils;


import com.android.jesse.log.Printer.Printer;
import com.android.jesse.log.util.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;



public class ALogger implements Logger {


    private static final int JSON_INDENT = 2;

    private final List<Printer> logPrinters = new ArrayList<>();

    public ALogger() {
    }

    @Override
    public void d(String tag, String message, Object... args) {
        log(DEBUG, null, tag, message, args);
    }

    @Override
    public void d(String tag, Object object) {
        log(DEBUG, null, tag, LogUtils.toString(object));
    }

    @Override
    public void e(String tag, String message, Object... args) {
        e(tag, null, message, args);
    }

    @Override
    public void e(String tag, Throwable throwable, String message, Object... args) {
        log(ERROR, throwable, tag, message, args);
    }

    @Override
    public void w(String tag, String message, Object... args) {
        log(WARN, null, tag, message, args);
    }

    @Override
    public void i(String tag, String message, Object... args) {
        log(INFO, null, tag, message, args);
    }

    @Override
    public void v(String tag, String message, Object... args) {
        log(VERBOSE, null, tag, message, args);
    }

    @Override
    public void wtf(String tag, String message, Object... args) {
        log(ASSERT, null, tag, message, args);
    }

    @Override
    public void json(String tag, String json) {
        if (TextUtils.isEmpty(json)) {
            d(tag, "Empty/Null json content");
            return;
        }
        try {
            json = json.trim();
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                String message = jsonObject.toString(JSON_INDENT);
                i(tag, message);
                return;
            }
            if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                String message = jsonArray.toString(JSON_INDENT);
                i(tag, message);
                return;
            }
            e(tag, "Invalid Json");
        } catch (JSONException e) {
            e(tag, "Invalid Json");
        }
    }

    @Override
    public void xml(String tag, String xml) {
        if (TextUtils.isEmpty(xml)) {
            d(tag, "Empty/Null xml content");
            return;
        }
        try {
            Source xmlInput = new StreamSource(new StringReader(xml));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(xmlInput, xmlOutput);
            i(tag, xmlOutput.getWriter().toString().replaceFirst(">", ">\n"));
        } catch (TransformerException e) {
            e(tag, "Invalid xml");
        }
    }

    @Override
    public void net(String tag, String message, Object... args) {
        log(NET, null, tag, message, args);
    }

    @Override
    public synchronized void log(int priority, String tag, String message, Throwable throwable) {
        if (throwable != null && message != null) {
            message += " : " + LogUtils.getStackTraceString(throwable);
        }
        if (throwable != null && message == null) {
            message = LogUtils.getStackTraceString(throwable);
        }
        if (TextUtils.isEmpty(message)) {
            message = "Empty/NULL log message";
        }

        for (Printer printer : logPrinters) {
            if (printer.isLoggable(priority, tag)) {
                printer.log(priority, tag, message);
            }
        }
    }

    @Override
    public void flush() {
        for (Printer printer : logPrinters) {
            printer.flush();
        }
    }

    @Override
    public void addPrinter(Printer printer) {
        logPrinters.add(printer);
    }

    @Override
    public List<Printer> getPrinters() {
        return logPrinters;
    }

    @Override
    public void clearLogPrinters() {

    }

    private synchronized void log(int priority, Throwable throwable, String tag, String msg, Object... args) {
        String message = createMessage(msg, args);
        log(priority, tag, message, throwable);
    }

    private String createMessage(String message, Object... args) {
        if (message == null) {
            return null;
        }
        return args == null || args.length == 0 ? message : String.format(message, args);
    }
}
