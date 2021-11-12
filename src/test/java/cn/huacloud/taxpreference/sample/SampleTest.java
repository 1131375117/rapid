package cn.huacloud.taxpreference.sample;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.huacloud.taxpreference.services.backup.Tax;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fasterxml.jackson.databind.util.ArrayIterator;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 简单测试类
 * @author wangkh
 */
@Slf4j
public class SampleTest {

@Test
public void testTax() throws Exception {
    List<Class<?>> allClassByInterface = getAllClassByInterface(Tax.class);
    System.out.println("实现"+Tax.class.getSimpleName()+"的类");
    for (int i = 0; i < allClassByInterface.size(); i++) {
      System.out.println(allClassByInterface.get(i).getSimpleName());
    }
}

  public static List<Object> getAllObjectByInterface(Class<?> c)
      throws InstantiationException, IllegalAccessException {
    List<Object> list = new ArrayList<Object>();
    List<Class<?>> classes = getAllClassByInterface(c);
    for (int i = 0; i < classes.size(); i++) {
      list.add(classes.get(i).newInstance());
    }
    return list;
        }

  /*
   * 获取指定接口的实例的Class对象
   */
  public static List<Class<?>> getAllClassByInterface(Class<?> c) {
    // 如果传入的参数不是接口直接结束
    if (!c.isInterface()) {
      return null;
    }

    // 获取当前包名
    String packageName = c.getPackage().getName();
    List<Class<?>> allClass = null;
    try {
      allClass = getAllClassFromPackage(packageName);
    } catch (ClassNotFoundException | IOException e) {
      e.printStackTrace();
    }

    ArrayList<Class<?>> list = new ArrayList<Class<?>>();
    for (int i = 0; i < allClass.size(); i++) {
      if (c.isAssignableFrom(allClass.get(i))) {
        if (!c.equals(allClass.get(i))) {
          list.add(allClass.get(i));
        }
      }
    }

    return list;
        }
    private static List<Class<?>> getAllClassFromPackage(String packageName) throws IOException, ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace(".", "/");
        Enumeration<URL> enumeration = classLoader.getResources(path);
        List<String> classNames = getClassNames(enumeration, packageName);

        ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
        for (int i = 0; i < classNames.size(); i++) {
            classes.add(Class.forName(classNames.get(i)));
        }

        return classes;
    }

    public static List<String> getClassNames(Enumeration<URL> enumeration, String packageName) {
        List<String> classNames = null;
        while (enumeration.hasMoreElements()) {
            URL url = enumeration.nextElement();
            if (url != null) {
                String type = url.getProtocol();
                if (type.equals("file")) {
                    System.out.println("type : file");
                    String fileSearchPath = url.getPath();
                    if(fileSearchPath.contains("META-INF")) {
                        System.out.println("continue + " + fileSearchPath);
                        continue;
                    }
                    classNames = getClassNameByFile(fileSearchPath);
                } else if (type.equals("jar")) {
                    try {
                        System.out.println("type : jar");
                        JarURLConnection jarURLConnection = (JarURLConnection)url.openConnection();
                        JarFile jarFile = jarURLConnection.getJarFile();
                        classNames = getClassNameByJar(jarFile, packageName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("type : none");
                }
            }
        }

        return classNames;
    }

    /*
     * 获取项目某路径下的所有类
     */
    public static List<String> getClassNameByFile(String fileSearchPath) {
        List<String> classNames = new ArrayList<String>();

        File file = new File(fileSearchPath);
        File[] childFiles = file.listFiles();
        for(File childFile : childFiles) {
            if(childFile.isDirectory()) {
                classNames.addAll(getClassNameByFile(childFile.getPath()));
            } else {
                String childFilePath = childFile.getPath();
                if (childFilePath.endsWith(".class")) {
                    String className = childFilePath.substring(childFilePath.lastIndexOf("\\bin\\") + 1,
                            childFilePath.length()).replaceAll("\\\\", ".");
                    className = className.substring(4, className.indexOf(".class"));
                    classNames.add(className);
                }
            }
        }

        return classNames;
    }

    /*
     * 从jar包中获取某路径下的所有类
     */
    public static List<String> getClassNameByJar(JarFile jarFile, String packageName) {
        List<String> classNames = new ArrayList<String>();
        Enumeration<JarEntry> entrys = jarFile.entries();
        while (entrys.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) entrys.nextElement();
            String entryName = jarEntry.getName();
            if(entryName.endsWith(".class")) {
                String className = entryName.replace("/", ".");
                className = className.substring(0, className.indexOf(".class"));
                classNames.add(className);
            }

        }
        return classNames;
    }



    @Test
    public void htmlTest() {
        LocalDate now = LocalDate.now();
        log.info(now.toString());
        log.info(LocalDateTime.now().toString());
    }

    @Test
    public void testPassword() {
        String patternStr = "(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[^a-zA-Z0-9]).{8,30}";
        Pattern pattern = Pattern.compile(patternStr);
        String password1 = "12345678Aa";
        String password2 = "12345678Aa!";

        Matcher matcher1 = pattern.matcher(password1);
        boolean find1 = matcher1.find();
        log.info("{} <=> {}", password1, find1);

        Matcher matcher2 = pattern.matcher(password2);
        boolean find2 = matcher2.find();
        log.info("{} <=> {}", password2, find2);
    }

    @Test
    public void testStreamFirst() {
        List<String> list = new ArrayList<>();
        Optional<String> first = list.stream().findFirst();
        log.info("isPresent: {}", first.isPresent());
    }

    @Test
    public void pinyin4jTest() throws Exception {
        String target = "选择增值税-一般纳税人";
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        StringBuilder sb = new StringBuilder();
        for (char ch : target.toCharArray()) {
            String[] strings = PinyinHelper.toHanyuPinyinStringArray(ch, format);
            if (strings == null) {
                sb.append(ch);
            } else {
                sb.append(strings[0].substring(0, 1));
            }
        }
        log.info(sb.toString());
    }

    @Test
    public void testStringJoin() {
        String join = String.join(",", new ArrayList<>());
        log.info("join: '{}'", join);
    }

    @Test
    public void testIdWorker() {
        for (int i = 0; i < 100; i++) {
            log.info(IdWorker.get32UUID());
        }
    }

    @Test
    public void testReflectMethod() {
        Method method = this.getClass().getDeclaredMethods()[0];
        log.info(method.toString());
        log.info(method.toGenericString());
    }

    @Test
    public void md5Test() {
        String md5 = SaSecureUtil.md5("123456");
        log.info(md5);
    }

    @Test
    public void testLogMessageFormat() {
        log.info("{}和{}是好友。", "小明", "小红");
    }
}
