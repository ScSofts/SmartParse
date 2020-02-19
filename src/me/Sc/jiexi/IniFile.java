package me.Sc.jiexi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

//INI�����ļ�

public class IniFile {

    /**
     * ���з�
     */
    private String line_separator = "\n\n";
    /**
     * ����
     */
    private String charSet = "";
    private Map<String, Section> sections = new LinkedHashMap<String, Section>();
    /**
     * ��ǰ�������ļ�����
     */
    private File file = null;

    public IniFile() {

    }

    public IniFile(File file) {
        this.file = file;
        initFromFile(file);
    }

    public IniFile(InputStream inputStream) {
        initFromInputStream(inputStream);
    }

    /**
     * ָ�����з�
     *
     * @param line_separator
     */
    public void setLineSeparator(String line_separator) {
        this.line_separator = line_separator;
    }

    /**
     * ָ������
     *
     * @param charSet
     */
    public void setCharSet(String charSet) {
        this.charSet = charSet;
    }

    /**
     * ����ֵ
     *
     * @param section �ڵ�
     * @param key     ������
     * @param value   ����ֵ
     */
    public void set(String section, String key, Object value) {
        Section sectionObject = sections.get(section);
        if (sectionObject == null)
            sectionObject = new Section();
        sectionObject.name = section;
        sectionObject.set(key, value);
        sections.put(section, sectionObject);
    }

    /**
     * ��ȡ�ڵ�
     *
     * @param section �ڵ�����
     * @return
     */
    public Section get(String section) {
        return sections.get(section);
    }

    /**
     * ��ȡֵ
     *
     * @param section �ڵ�����
     * @param key     ��������
     * @return
     */
    public Object get(String section, String key) {
        return get(section, key, null);
    }

    /**
     * ��ȡֵ
     *
     * @param section      �ڵ�����
     * @param key          ��������
     * @param defaultValue ���Ϊ�շ���Ĭ��ֵ
     * @return
     */
    public Object get(String section, String key, String defaultValue) {
        Section sectionObject = sections.get(section);
        if (sectionObject != null) {
            Object value = sectionObject.get(key);
            if (value == null || value.toString().trim().equals(""))
                return defaultValue;
            return value;
        }
        return null;
    }

    /**
     * ɾ���ڵ�
     *
     * @param section �ڵ�����
     */
    public void remove(String section) {
        sections.remove(section);
    }

    /**
     * ɾ������
     *
     * @param section �ڵ�����
     * @param key     ��������
     */
    public void remove(String section, String key) {
        Section sectionObject = sections.get(section);
        if (sectionObject != null)
            sectionObject.getValues().remove(key);
    }

    /**
     * ����һ��ini�ļ�
     *
     * @param file
     */
    public void load(File file) {
        this.file = file;
        initFromFile(file);
    }

    /**
     * ����һ��������
     *
     * @param inputStream
     */
    public void load(InputStream inputStream) {
        initFromInputStream(inputStream);
    }

    /**
     * д���������
     *
     * @param outputStream
     */
    public void save(OutputStream outputStream) {
        BufferedWriter bufferedWriter;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(
                    outputStream, charSet));
            saveConfig(bufferedWriter);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * ���浽�ļ�
     *
     * @param file
     */
    public void save(File file) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(
                    file));
            saveConfig(bufferedWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ���浽��ǰ�ļ�
     */
    public void save() {
        save(this.file);
    }

    /**
     * ����������ʼ��IniFile
     *
     * @param inputStream
     */
    private void initFromInputStream(InputStream inputStream) {
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(
                    inputStream, charSet));
            toIniFile(bufferedReader);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * ���ļ���ʼ��IniFile
     *
     * @param file
     */
    private void initFromFile(File file) {
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            toIniFile(bufferedReader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * ��BufferedReader ��ʼ��IniFile
     *
     * @param bufferedReader
     */
    private void toIniFile(BufferedReader bufferedReader) {
        String strLine;
        Section section = null;
        Pattern p = Pattern.compile("^\\[.*\\]$");
        try {
            while ((strLine = bufferedReader.readLine()) != null) {
                if (p.matcher((strLine)).matches()) {
                    strLine = strLine.trim();
                    section = new Section();
                    section.name = strLine.substring(1, strLine.length() - 1);
                    sections.put(section.name, section);
                } else {
                    String[] keyValue = strLine.split("=");
                    if (keyValue.length >= 2) {
                        for (int i = 2; i < keyValue.length; i++)
                            keyValue[1] += keyValue[i];
                        section.set(keyValue[0], keyValue[1]);
                    }
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ����Ini�ļ�
     *
     * @param bufferedWriter
     */
    private void saveConfig(BufferedWriter bufferedWriter) {
        try {
            boolean line_spe = true;
            if (line_separator == null || line_separator.trim().equals(""))
                line_spe = false;
            for (Section section : sections.values()) {
                bufferedWriter.write("[" + section.getName() + "]");
                if (line_spe)
                    bufferedWriter.write(line_separator);
                else
                    bufferedWriter.newLine();
                for (Map.Entry<String, Object> entry : section.getValues()
                        .entrySet()) {
                    bufferedWriter.write(entry.getKey());
                    bufferedWriter.write("=");
                    bufferedWriter.write(entry.getValue().toString());
                    System.out.println(entry.getValue());
                    if (line_spe)
                        bufferedWriter.write(line_separator);
                    else
                        bufferedWriter.newLine();
                }
            }
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class Section {

        private String name;

        private Map<String, Object> values = new LinkedHashMap<String, Object>();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void set(String key, Object value) {
            values.put(key, value);
        }

        public Object get(String key) {
            return values.get(key);
        }

        public Map<String, Object> getValues() {
            return values;
        }

    }

}
