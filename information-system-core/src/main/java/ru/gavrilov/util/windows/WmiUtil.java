package ru.gavrilov.util.windows;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.platform.win32.Variant.VARIANT;
import com.sun.jna.platform.win32.WTypes.BSTR;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gavrilov.jna.platform.windows.COM.EnumWbemClassObject;
import ru.gavrilov.jna.platform.windows.COM.WbemClassObject;
import ru.gavrilov.jna.platform.windows.COM.WbemLocator;
import ru.gavrilov.jna.platform.windows.COM.WbemServices;
import ru.gavrilov.jna.platform.windows.Ole32;
import ru.gavrilov.util.FormatUtil;
import ru.gavrilov.util.ParseUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class WmiUtil {
    private static final Logger LOG = LoggerFactory.getLogger(WmiUtil.class);

    public static final String DEFAULT_NAMESPACE = "ROOT\\CIMV2";

    private static boolean comInitialized = false;
    private static boolean securityInitialized = false;

    public enum ValueType {
        // Properties
        STRING, UINT32, FLOAT, DATETIME, BOOLEAN, UINT64, UINT16,
        // Methods (use "__PATH" for property)
        PROCESS_GETOWNER, PROCESS_GETOWNERSID
    }

    private static final ValueType[] STRING_TYPE = { ValueType.STRING };
    private static final ValueType[] UINT32_TYPE = { ValueType.UINT32 };
    private static final ValueType[] FLOAT_TYPE = { ValueType.FLOAT };

    public static boolean hasNamespace(String namespace) {
        Map<String, List<String>> nsMap = WmiUtil.selectStringsFrom("ROOT", "__NAMESPACE", "Name", null);
        for (String s : nsMap.get("Name")) {
            if (s.equals(namespace)) {
                return true;
            }
        }
        return false;
    }

    public static Long selectUint32From(String namespace, String wmiClass, String property, String whereClause) {
        Map<String, List<Object>> result = queryWMI(namespace == null ? DEFAULT_NAMESPACE : namespace, property,
                wmiClass, whereClause, UINT32_TYPE);
        if (result.containsKey(property) && !result.get(property).isEmpty()) {
            return (Long) result.get(property).get(0);
        }
        return 0L;
    }

    public static Map<String, List<Long>> selectUint32sFrom(String namespace, String wmiClass, String properties,
            String whereClause) {
        Map<String, List<Object>> result = queryWMI(namespace == null ? DEFAULT_NAMESPACE : namespace, properties,
                wmiClass, whereClause, UINT32_TYPE);
        HashMap<String, List<Long>> longMap = new HashMap<>();
        for (Entry<String, List<Object>> entry : result.entrySet()) {
            ArrayList<Long> longList = new ArrayList<>();
            for (Object obj : entry.getValue()) {
                longList.add((Long) obj);
            }
            longMap.put(entry.getKey(), longList);
        }
        return longMap;
    }

    public static Float selectFloatFrom(String namespace, String wmiClass, String property, String whereClause) {
        Map<String, List<Object>> result = queryWMI(namespace == null ? DEFAULT_NAMESPACE : namespace, property,
                wmiClass, whereClause, FLOAT_TYPE);
        if (result.containsKey(property) && !result.get(property).isEmpty()) {
            return (Float) result.get(property).get(0);
        }
        return 0f;
    }

    public static Map<String, List<Float>> selectFloatsFrom(String namespace, String wmiClass, String properties,
            String whereClause) {
        Map<String, List<Object>> result = queryWMI(namespace == null ? DEFAULT_NAMESPACE : namespace, properties,
                wmiClass, whereClause, FLOAT_TYPE);
        HashMap<String, List<Float>> floatMap = new HashMap<>();
        for (Entry<String, List<Object>> entry : result.entrySet()) {
            ArrayList<Float> floatList = new ArrayList<>();
            for (Object obj : entry.getValue()) {
                floatList.add((Float) obj);
            }
            floatMap.put(entry.getKey(), floatList);
        }
        return floatMap;
    }

    public static String selectStringFrom(String namespace, String wmiClass, String property, String whereClause) {
        Map<String, List<Object>> result = queryWMI(namespace == null ? DEFAULT_NAMESPACE : namespace, property,
                wmiClass, whereClause, STRING_TYPE);
        if (result.containsKey(property) && !result.get(property).isEmpty()) {
            return (String) result.get(property).get(0);
        }
        return "";
    }

    public static Map<String, List<String>> selectStringsFrom(String namespace, String wmiClass, String properties,
            String whereClause) {
        Map<String, List<Object>> result = queryWMI(namespace == null ? DEFAULT_NAMESPACE : namespace, properties,
                wmiClass, whereClause, STRING_TYPE);
        HashMap<String, List<String>> strMap = new HashMap<>();
        for (Entry<String, List<Object>> entry : result.entrySet()) {
            ArrayList<String> strList = new ArrayList<>();
            for (Object obj : entry.getValue()) {
                strList.add((String) obj);
            }
            strMap.put(entry.getKey(), strList);
        }
        return strMap;
    }

    /**
     * Получать множественные индивидуально типизированные значения из WMI
     *
     * @param namespace
     * Пространство имен или значение null для использования значения по умолчанию
     * @param wmiClass
     * Класс для запроса
     * @param properties
     * Список объектов с разделителями-запятыми, значение которых возвращается
     * @param whereClause
     * WQL, где свойства соответствия и ключевые слова
     * @param propertyTypes
     * Массив типов, соответствующих свойствам, или один
     * массив элементов
     * @return Карта с каждым свойством в качестве ключа, содержащим объекты с
     * значение запрашиваемых свойств. Порядок каждого списка соответствует
     * в другие списки. Тип объектов идентифицируется
     * propertyTypes array. Если задано только одно свойствоType, все
     * Объекты будут иметь этот тип. Ответственность за
     * caller для возврата возвращаемых объектов.
     */
    public static Map<String, List<Object>> selectObjectsFrom(String namespace, String wmiClass, String properties,
            String whereClause, ValueType[] propertyTypes) {
        return queryWMI(namespace == null ? DEFAULT_NAMESPACE : namespace, properties, wmiClass, whereClause,
                propertyTypes);
    }

    /**
     * Запрос WMI для значений
     *
     * @param namespace
     * Пространство имен для запроса
     * @param properties
     * Единственное свойство или список свойств, разделенных запятыми
     * перечислять
     * @param wmiClass
     * Класс WMI для запроса
     * @param propertyTypes
     * Массив, соответствующий свойствам, содержащим тип
     * запрашиваемых данных, чтобы контролировать, как анализируется VARIANT
     * @return Карта с строковым значением каждого свойства в качестве ключа,
     *, содержащий список объектов, которые могут быть выбраны соответствующим образом
     * valType. Порядок объектов в каждом списке соответствует
     * другие списки.
     */
    private static Map<String, List<Object>> queryWMI(String namespace, String properties, String wmiClass,
            String whereClause, ValueType[] propertyTypes) {
        // Set up empty map
        Map<String, List<Object>> values = new HashMap<>();
        String[] props = properties.split(",");
        for (int i = 0; i < props.length; i++) {
            if ("__PATH".equals(props[i])) {
                // Methods will query __PATH
                values.put(propertyTypes[i].name(), new ArrayList<>());
            } else {
                // Properties are named
                values.put(props[i], new ArrayList<>());
            }
        }

        if (!initCOM()) {
            unInitCOM();
            return values;
        }

        PointerByReference pSvc = new PointerByReference();
        if (!connectServer(namespace, pSvc)) {
            unInitCOM();
            return values;
        }
        WbemServices svc = new WbemServices(pSvc.getValue());

        PointerByReference pEnumerator = new PointerByReference();
        if (!selectProperties(svc, pEnumerator, properties, wmiClass, whereClause)) {
            svc.Release();
            unInitCOM();
            return values;
        }
        EnumWbemClassObject enumerator = new EnumWbemClassObject(pEnumerator.getValue());

        enumerateProperties(values, enumerator, props, propertyTypes, svc);

        // Cleanup
        enumerator.Release();
        svc.Release();
        unInitCOM();
        return values;
    }

    private static boolean initCOM() {
        // Step 1: --------------------------------------------------
        // Initialize COM. ------------------------------------------
        HRESULT hres = Ole32.INSTANCE.CoInitializeEx(null, Ole32.COINIT_MULTITHREADED);
        if (COMUtils.FAILED(hres)) {
            if (hres.intValue() == Ole32.RPC_E_CHANGED_MODE) {
                // Com already initialized, ignore error
                LOG.debug("COM already initialized.");
                securityInitialized = true;
                return true;
            }
            LOG.error(String.format("Failed to initialize COM library. Error code = 0x%08x", hres.intValue()));
            return false;
        }
        comInitialized = true;
        if (securityInitialized) {
            // Only run CoInitializeSecuirty once
            return true;
        }
        // Step 2: --------------------------------------------------
        // Set general COM security levels --------------------------
        hres = Ole32.INSTANCE.CoInitializeSecurity(null, new NativeLong(-1), null, null,
                Ole32.RPC_C_AUTHN_LEVEL_DEFAULT, Ole32.RPC_C_IMP_LEVEL_IMPERSONATE, null, Ole32.EOAC_NONE, null);
        if (COMUtils.FAILED(hres) && hres.intValue() != Ole32.RPC_E_TOO_LATE) {
            LOG.error(String.format("Failed to initialize security. Error code = 0x%08x", hres.intValue()));
            Ole32.INSTANCE.CoUninitialize();
            return false;
        }
        securityInitialized = true;
        return true;
    }

    private static boolean connectServer(String namespace, PointerByReference pSvc) {
        // Step 3: ---------------------------------------------------
        // Obtain the initial locator to WMI -------------------------
        WbemLocator loc = WbemLocator.create();
        if (loc == null) {
            return false;
        }
        // Step 4: -----------------------------------------------------
        // Connect to WMI through the IWbemLocator::ConnectServer method
        // Connect to the namespace with the current user and obtain pointer
        // pSvc to make IWbemServices calls.
        HRESULT hres = loc.ConnectServer(new BSTR(namespace), null, null, null, null, null, null, pSvc);
        if (COMUtils.FAILED(hres)) {
            LOG.error(String.format("Could not connect to namespace %s. Error code = 0x%08x", namespace,
                    hres.intValue()));
            loc.Release();
            unInitCOM();
            return false;
        }
        LOG.debug("Connected to {} WMI namespace", namespace);
        loc.Release();

        // Step 5: --------------------------------------------------
        // Set security levels on the proxy -------------------------
        hres = Ole32.INSTANCE.CoSetProxyBlanket(pSvc.getValue(), Ole32.RPC_C_AUTHN_WINNT, Ole32.RPC_C_AUTHZ_NONE, null,
                Ole32.RPC_C_AUTHN_LEVEL_CALL, Ole32.RPC_C_IMP_LEVEL_IMPERSONATE, null, Ole32.EOAC_NONE);
        if (COMUtils.FAILED(hres)) {
            LOG.error(String.format("Could not set proxy blanket. Error code = 0x%08x", hres.intValue()));
            new WbemServices(pSvc.getValue()).Release();
            unInitCOM();
            return false;
        }
        return true;
    }

    /**
     * Выбирает свойства из WMI. Возвращается немедленно, даже если результаты
     * извлекается; результаты могут быть перечислены в
     * направление только.
     *
     * @param svc
     * Объект WbemServices для совершения вызовов
     * @param pEnumerator
     * Перечислитель для получения результатов запроса
     * @param properties
     * Список свойств для запроса
     * @param wmiClass
     * Класс WMI для запроса
     * @param whereClause
     * Предложение WHERE для сужения запроса
     * @ верните правду в случае успеха. Перечислитель позволит перечислять
     * результаты запроса
     */
    private static boolean selectProperties(WbemServices svc, PointerByReference pEnumerator, String properties,
            String wmiClass, String whereClause) {
        String query = String.format("SELECT %s FROM %s %s", properties, wmiClass,
                whereClause != null ? whereClause : "");
        LOG.debug("Query: {}", query);
        HRESULT hres = svc.ExecQuery(new BSTR("WQL"), new BSTR(query),
                new NativeLong(
                        EnumWbemClassObject.WBEM_FLAG_FORWARD_ONLY | EnumWbemClassObject.WBEM_FLAG_RETURN_IMMEDIATELY),
                null, pEnumerator);
        if (COMUtils.FAILED(hres)) {
            LOG.error(String.format("Query '%s' failed. Error code = 0x%08x", query, hres.intValue()));
            svc.Release();
            unInitCOM();
            return false;
        }
        return true;
    }

    private static void enumerateProperties(Map<String, List<Object>> values, EnumWbemClassObject enumerator,
            String[] properties, ValueType[] propertyTypes, WbemServices svc) {
        if (propertyTypes.length > 1 && properties.length != propertyTypes.length) {
            throw new IllegalArgumentException("Property type array size must be 1 or equal to properties array size.");
        }

        PointerByReference pclsObj = new PointerByReference();
        LongByReference uReturn = new LongByReference(0L);
        while (enumerator.getPointer() != Pointer.NULL) {
            HRESULT hres = enumerator.Next(new NativeLong(EnumWbemClassObject.WBEM_INFINITE), new NativeLong(1),
                    pclsObj, uReturn);
            if (0L == uReturn.getValue() || COMUtils.FAILED(hres)) {
                return;
            }
            VARIANT.ByReference vtProp = new VARIANT.ByReference();

            WbemClassObject clsObj = new WbemClassObject(pclsObj.getValue());
            for (int p = 0; p < properties.length; p++) {
                String property = properties[p];
                hres = clsObj.Get(new BSTR(property), new NativeLong(0L), vtProp, null, null);

                ValueType propertyType = propertyTypes.length > 1 ? propertyTypes[p] : propertyTypes[0];
                switch (propertyType) {
                case STRING:
                    values.get(property).add(vtProp.getValue() == null ? "unknown" : vtProp.stringValue());
                    break;
                case UINT16:
                    values.get(property).add(vtProp.getValue() == null ? 0L : vtProp.intValue());
                    break;
                case UINT32:
                    values.get(property).add(vtProp.getValue() == null ? 0L : vtProp.longValue());
                    break;
                case UINT64:
                    values.get(property).add(
                            vtProp.getValue() == null ? 0L : ParseUtil.parseLongOrDefault(vtProp.stringValue(), 0L));
                    break;
                case FLOAT:
                    values.get(property).add(vtProp.getValue() == null ? 0f : vtProp.floatValue());
                    break;
                case DATETIME:
                    values.get(property)
                            .add(vtProp.getValue() == null ? 0L : ParseUtil.cimDateTimeToMillis(vtProp.stringValue()));
                    break;
                case BOOLEAN:
                    values.get(property).add(vtProp.getValue() == null ? 0L : vtProp.booleanValue());
                    break;
                case PROCESS_GETOWNER:
                    String owner = FormatUtil.join("\\",
                            execMethod(svc, vtProp.stringValue(), "GetOwner", "Domain", "User"));
                    values.get(propertyType.name()).add("\\".equals(owner) ? "N/A" : owner);
                    break;
                case PROCESS_GETOWNERSID:
                    String[] ownerSid = execMethod(svc, vtProp.stringValue(), "GetOwnerSid", "Sid");
                    values.get(propertyType.name()).add(ownerSid.length < 1 ? "" : ownerSid[0]);
                    break;
                default:
                    throw new IllegalArgumentException("Unimplemented enum type: " + propertyType.toString());
                }
                OleAuto.INSTANCE.VariantClear(vtProp);
            }

            clsObj.Release();
        }
    }

    private static void unInitCOM() {
        if (comInitialized) {
            Ole32.INSTANCE.CoUninitialize();
            comInitialized = false;
        }
    }

    /**
     * Удобный метод для выполнения WMI-методов без каких-либо входных параметров
     *
     * @param svc
     * Объект WbemServices
     * @param clsObj
     * Полный путь к объекту класса для выполнения (результат WMI
     * Запрос «__PATH»)
     * @param method
     * Имя метода для выполнения
     * @param properties
     * Один или несколько свойств, возвращаемых в результате запроса
     * @return Массив свойств, возвращаемых методом
     */
    private static String[] execMethod(WbemServices svc, String clsObj, String method, String... properties) {
        List<String> result = new ArrayList<>();
        PointerByReference ppOutParams = new PointerByReference();
        HRESULT hres = svc.ExecMethod(new BSTR(clsObj), new BSTR(method), new NativeLong(0L), null, null, ppOutParams,
                null);
        if (COMUtils.FAILED(hres)) {
            return new String[0];
        }
        WbemClassObject obj = new WbemClassObject(ppOutParams.getValue());
        VARIANT.ByReference vtProp = new VARIANT.ByReference();
        for (String prop : properties) {
            hres = obj.Get(new BSTR(prop), new NativeLong(0L), vtProp, null, null);
            if (!COMUtils.FAILED(hres)) {
                result.add(vtProp.getValue() == null ? "" : vtProp.stringValue());
            }
        }
        obj.Release();
        return result.toArray(new String[result.size()]);
    }

}
