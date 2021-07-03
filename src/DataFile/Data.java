package DataFile;

import RecordFile.Record;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class Data implements Serializable {

    int count = 0;
    ConcurrentHashMap<Character, ArrayList<Record>> HashMapData = new ConcurrentHashMap<Character, ArrayList<Record>>();

}
