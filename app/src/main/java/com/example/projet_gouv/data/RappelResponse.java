package com.example.projet_gouv.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RappelResponse {

    @SerializedName("records")
    private List<Record> records;

    public List<Record> getRecords() {
        return records;
    }

    public static class Record {
        @SerializedName("record")
        private Fields fields;

        public Fields getFields() {
            return fields;
        }

        public static class Fields {
            @SerializedName("fields")
            private Rappel rappel;

            public Rappel getRappel() {
                return rappel;
            }
        }
    }
}
