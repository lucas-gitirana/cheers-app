package com.example.cheers.data.db.converter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Converters {

    /**
     * Converte uma String JSON em uma Lista de Strings.
     * Room usará este método ao ler do banco de dados.
     * @param value A String JSON vinda do banco.
     * @return Uma Lista de Strings.
     */
    @TypeConverter
    public static List<String> fromString(String value) {
        if (value == null) {
            return null;
        }
        // Use o TypeToken diretamente do Gson
        Type listType = new TypeToken<List<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }
    /**
     * Converte uma Lista de Strings para uma única String em formato JSON.
     * Room usará este método ao salvar no banco de dados.
     * @param list A lista de strings a ser convertida.
     * @return Uma String em formato JSON.
     */
    @TypeConverter
    public static String fromList(List<String> list) {
        if (list == null) {
            return null;
        }
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}
