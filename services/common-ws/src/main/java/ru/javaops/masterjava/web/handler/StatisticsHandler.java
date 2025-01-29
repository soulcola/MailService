package ru.javaops.masterjava.web.handler;


import com.sun.xml.ws.api.handler.MessageHandlerContext;
import jakarta.xml.ws.handler.MessageContext;
import lombok.extern.slf4j.Slf4j;
import ru.javaops.masterjava.web.Statistics;

@Slf4j
public class StatisticsHandler extends SoapBaseHandler {
    private static final String PAYLOAD = "PAYLOAD";
    private static final String START_TIME = "START_TIME";

    @Override
    public boolean handleMessage(MessageHandlerContext mhc) {
        if (!isOutbound(mhc)) {
            long startTime = System.currentTimeMillis();
            mhc.put(START_TIME, startTime);
            mhc.put(PAYLOAD, mhc.getMessage().getPayloadLocalPart());
        } else {
            Long startTime = (Long) mhc.get(START_TIME);
            String payload = (String) mhc.get(PAYLOAD);
            Statistics.RESULT result = Statistics.RESULT.SUCCESS;
            Statistics.count(payload, startTime, result);
        }
        return true;
    }

    @Override
    public boolean handleFault(MessageHandlerContext mhc) {
        Long startTime = (Long) mhc.get(START_TIME);
        Statistics.RESULT result = Statistics.RESULT.FAIL;
        Statistics.count(mhc.getMessage().getPayloadLocalPart(), startTime, result);
        return true;
    }
}
