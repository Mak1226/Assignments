package processor.memorysystem;
import configuration.Configuration;
//import generic.Instruction;
import generic.*;
import processor.Clock;
import processor.Processor;
import processor.pipeline.MemoryAccess;

public class Cache implements Element 
{
    Processor containingProcessor;
    int cSize, Late;
    CacheLine[] aCache;
    int lines, sets, missAd, wData;
    Element missEl;
    Boolean read;
    //Instruction instruction

    public Cache(Processor processor, int size)
    {
        this.containingProcessor = processor;
        cSize = size;
        lines = size / 4;
        sets = lines / 2;

        switch(size){
            case 8:
                Late = 1;
                break;
            case 32:
                Late = 2;
                break;
            case 128:
                Late = 4;
                break;
            case 1024:
                Late = 8;
                break;
        }
        aCache = new CacheLine[lines];
        for(int i = 0; i < lines; i++)
            aCache[i] = new CacheLine();
    }

    public int getCacheLatency()
    {
        return Late;
    }

    public static String toBinary(int x, int len)
    {
        if (len > 0) 
            return String.format("%" + len + "s", Integer.toBinaryString(x)).replace(" ", "0");
        
            return null;
    }

    public void handleCacheMiss(int address, Element requestingElement)
    {
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

    public void handleResponse(int value)
    {
        String addressString = toBinary(missAd, 32);
        int indexBits = (int) (Math.log(lines) / Math.log(2));
        int cacheAddress;
        if(indexBits == 0)
            cacheAddress = 0;
        else
        {
            assert addressString != null;
            cacheAddress = Integer.parseInt(addressString.substring((32 - indexBits), 32), 2);
            //cacheAddress = instruction >> (32 - indexBits)
        }
        aCache[cacheAddress].setData(value);
        aCache[cacheAddress].setTag(missAd);
        if(read)
            Simulator.getEventQueue().addEvent(new MemoryResponseEvent(Clock.getCurrentTime(), this, missEl, value));
        
        else
            cacheWrite(missAd, wData, missEl);
    }

    public void cacheRead(int address, Element requestingElement)
    {
        String addressString = toBinary(address, 32);
        int cacheAddress;
        int indexBits = (int) (Math.log(lines) / Math.log(2));
        if(indexBits == 0) 
            cacheAddress = 0;
        
        else
        {
            assert addressString != null;
            cacheAddress = Integer.parseInt(addressString.substring((32 - indexBits), 32), 2);
            //cacheAddress = instruction >> (32 - indexBits)
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
        else
        {
            read = true;
            handleCacheMiss(address, requestingElement);
        }
    }

    public void cacheWrite(int address, int value, Element requestingElement)
    {
        String addressString = toBinary(address, 32);
        int cacheAddress;
        int indexBits = (int) (Math.log(lines) / Math.log(2));
        if(indexBits == 0)
            cacheAddress = 0;

        else
        {
            assert addressString != null;
            cacheAddress = Integer.parseInt(addressString.substring((32 - indexBits), 32), 2);
            //cacheAddress = instruction >> (32 - indexBits)

        }
        int cacheTag = aCache[cacheAddress].getTag();
        if(cacheTag == address)
        {
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
            ((MemoryAccess)requestingElement).EX_MA_Latch.set_MA_Busy(false);
            ((MemoryAccess)requestingElement).MA_RW_Latch.set_RW_enable(true);
        }
        else
        {
            read = false;
            wData = value;
            handleCacheMiss(address, requestingElement);
        }
    }

    @Override
    public void handleEvent(Event e) 
    {
        if(e.getEventType() == Event.EventType.MemoryRead)
        {
            MemoryReadEvent event = (MemoryReadEvent) e;
            cacheRead(event.getAddressToReadFrom(), event.getRequestingElement());
        }
        else if(e.getEventType() == Event.EventType.MemoryResponse)
        {
            MemoryResponseEvent event = (MemoryResponseEvent) e;
            handleResponse(event.getValue());
        }
        else if(e.getEventType() == Event.EventType.MemoryWrite)
        {
            MemoryWriteEvent event = (MemoryWriteEvent) e;
            cacheWrite(event.getAddressToWriteTo(), event.getValue(), event.getRequestingElement());
        }
    }
}