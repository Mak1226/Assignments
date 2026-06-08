package processor.memorysystem;
import configuration.Configuration;
import generic.*;
import processor.Clock;
import processor.Processor;
import processor.pipeline.MemoryAccess;

public class Cache implements Element {
    Processor containingProcessor;
    int cSize;
    int cLatency;
    CacheLine[] aCache;
    int lines;
    int sets;
    int missAd;
    Element missEl;
    Boolean read;
    int wData;

    public Cache(Processor processor, int size){
        this.containingProcessor = processor;
        this.cSize = size;
        this.lines = size / 4;
        this.sets = lines / 2;

        switch(size){
            case 8:
                cLatency = 1;
                break;
            case 32:
                cLatency = 2;
                break;
            case 128:
                cLatency = 4;
                break;
            case 1024:
                cLatency = 8;
                break;
        }
        aCache = new CacheLine[lines];
        for(int i = 0; i < lines; i++)
            aCache[i] = new CacheLine();
    }

    public int getcLatency() { return cLatency; }

    public static String toBinary(int x, int len){
        if (len > 0) {
            return String.format("%" + len + "s",
                    Integer.toBinaryString(x)).replace(" ", "0");
        }
        return null;
    }

    public void handleCacheMiss(int address, Element requestingElement){
        Simulator.getEventQueue().addEvent(
                new MemoryReadEvent(
                        Clock.getCurrentTime() + Configuration.mainMemoryLatency,
                        this,
                        containingProcessor.getMainMemory(),
                        address
                )
        );
        missAd = address;
        missEl = requestingElement;
    }

    public void handleResponse(int value){
        String addressString = toBinary(missAd, 32);
        int indexBits = (int) (Math.log(lines) / Math.log(2));
        int cacheAddress;
        if(indexBits == 0){
            cacheAddress = 0;
        }
        else{
            assert addressString != null;
            cacheAddress = Integer.parseInt(addressString.substring((32 - indexBits), 32), 2);
        }
        aCache[cacheAddress].setData(value);
        aCache[cacheAddress].setTag(missAd);
        if(read){
            Simulator.getEventQueue().addEvent(
                    new MemoryResponseEvent(
                            Clock.getCurrentTime(),
                            this,
                            missEl,
                            value
                    )
            );
        }
        else{
            cacheWrite(missAd, wData, missEl);
        }
    }

    public void cacheRead(int address, Element requestingElement){
        String addressString = toBinary(address, 32);
        int cacheAddress;
        int indexBits = (int) (Math.log(lines) / Math.log(2));
        if(indexBits == 0) {
            cacheAddress = 0;
        }
        else{
            assert addressString != null;
            cacheAddress = Integer.parseInt(addressString.substring((32 - indexBits), 32), 2);
        }
        int cacheTag = aCache[cacheAddress].getTag();
        if(cacheTag == address){
            Simulator.getEventQueue().addEvent(
                    new MemoryResponseEvent(
                            Clock.getCurrentTime(),
                            this,
                            requestingElement,
                            aCache[cacheAddress].getData()
                    )
            );
        }
        else{
            read = true;
            handleCacheMiss(address, requestingElement);
        }
    }

    public void cacheWrite(int address, int value, Element requestingElement){
        String addressString = toBinary(address, 32);
        int cacheAddress;
        int indexBits = (int) (Math.log(lines) / Math.log(2));
        if(indexBits == 0){
            cacheAddress = 0;
        }
        else{
            assert addressString != null;
            cacheAddress = Integer.parseInt(addressString.substring((32 - indexBits), 32), 2);
        }
        int cacheTag = aCache[cacheAddress].getTag();
        if(cacheTag == address){
            aCache[cacheAddress].setData(value);
            Simulator.getEventQueue().addEvent(
                    new MemoryWriteEvent(
                            Clock.getCurrentTime(),
                            this,
                            containingProcessor.getMainMemory(),
                            address,
                            value
                    )
            );
            ((MemoryAccess)requestingElement).EX_MA_Latch.setMA_Busy(false);
            ((MemoryAccess)requestingElement).MA_RW_Latch.setRW_enable(true);
        }
        else{
            read = false;
            wData = value;
            handleCacheMiss(address, requestingElement);
        }
    }

    @Override
    public void handleEvent(Event e) {
        if(e.getEventType() == Event.EventType.MemoryRead){
            MemoryReadEvent event = (MemoryReadEvent) e;
            cacheRead(event.getAddressToReadFrom(), event.getRequestingElement());
        }
        else if(e.getEventType() == Event.EventType.MemoryResponse){
            MemoryResponseEvent event = (MemoryResponseEvent) e;
            handleResponse(event.getValue());
        }
        else if(e.getEventType() == Event.EventType.MemoryWrite){
            MemoryWriteEvent event = (MemoryWriteEvent) e;
            cacheWrite(event.getAddressToWriteTo(), event.getValue(), event.getRequestingElement());
        }
    }
}