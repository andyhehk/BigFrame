/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package bigframe.avro;

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public interface SparkParquetAvro_UM {
  public static final org.apache.avro.Protocol PROTOCOL = org.apache.avro.Protocol.parse("{\"protocol\":\"SparkParquetAvro_UM\",\"namespace\":\"bigframe.avro\",\"types\":[{\"type\":\"record\",\"name\":\"User_Mention\",\"fields\":[{\"name\":\"id\",\"type\":[\"null\",\"long\"]},{\"name\":\"id_str\",\"type\":[\"null\",\"string\"]},{\"name\":\"indices\",\"type\":[\"null\",{\"type\":\"array\",\"items\":\"int\"}]},{\"name\":\"name\",\"type\":[\"null\",\"string\"]},{\"name\":\"screen_name\",\"type\":[\"null\",\"string\"]}]}],\"messages\":{}}");

  @SuppressWarnings("all")
  public interface Callback extends SparkParquetAvro_UM {
    public static final org.apache.avro.Protocol PROTOCOL = bigframe.avro.SparkParquetAvro_UM.PROTOCOL;
  }
}