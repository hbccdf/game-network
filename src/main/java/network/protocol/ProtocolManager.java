package network.protocol;

import java.util.HashMap;

public class ProtocolManager {
    private static final HashMap<Class<?>, Integer> classAndIdMap = new HashMap<>();
    private static final HashMap<Integer, Class<?>> idAndClassMap = new HashMap<>();

    public static void register(Class<?> clz, Integer protocolId) {
        classAndIdMap.put(clz, protocolId);
        idAndClassMap.put(protocolId, clz);
    }

    public static Integer getId(Class<?> clz) {
        return classAndIdMap.get(clz);
    }

    public static Class<?> getClass(Integer protocolId) {
        return idAndClassMap.get(protocolId);
    }
}
