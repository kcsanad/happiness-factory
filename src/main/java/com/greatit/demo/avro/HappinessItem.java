/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.greatit.demo.avro;

import org.apache.avro.generic.GenericArray;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

/** Item produced by the Happiness machine */
@org.apache.avro.specific.AvroGenerated
public class HappinessItem extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -6935476231958936052L;


  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"HappinessItem\",\"namespace\":\"com.greatit.demo.avro\",\"doc\":\"Item produced by the Happiness machine\",\"fields\":[{\"name\":\"id\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"doc\":\"Id of a happiness item \"},{\"name\":\"datetime\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"doc\":\"Time when happiness was made\"},{\"name\":\"level\",\"type\":\"int\",\"doc\":\"How strong this happiness is?\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static final SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<HappinessItem> ENCODER =
      new BinaryMessageEncoder<>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<HappinessItem> DECODER =
      new BinaryMessageDecoder<>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<HappinessItem> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<HappinessItem> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<HappinessItem> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this HappinessItem to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a HappinessItem from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a HappinessItem instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static HappinessItem fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  /** Id of a happiness item  */
  private java.lang.String id;
  /** Time when happiness was made */
  private java.lang.String datetime;
  /** How strong this happiness is? */
  private int level;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public HappinessItem() {}

  /**
   * All-args constructor.
   * @param id Id of a happiness item 
   * @param datetime Time when happiness was made
   * @param level How strong this happiness is?
   */
  public HappinessItem(java.lang.String id, java.lang.String datetime, java.lang.Integer level) {
    this.id = id;
    this.datetime = datetime;
    this.level = level;
  }

  @Override
  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }

  @Override
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }

  // Used by DatumWriter.  Applications should not call.
  @Override
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return id;
    case 1: return datetime;
    case 2: return level;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @Override
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: id = value$ != null ? value$.toString() : null; break;
    case 1: datetime = value$ != null ? value$.toString() : null; break;
    case 2: level = (java.lang.Integer)value$; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'id' field.
   * @return Id of a happiness item 
   */
  public java.lang.String getId() {
    return id;
  }


  /**
   * Sets the value of the 'id' field.
   * Id of a happiness item 
   * @param value the value to set.
   */
  public void setId(java.lang.String value) {
    this.id = value;
  }

  /**
   * Gets the value of the 'datetime' field.
   * @return Time when happiness was made
   */
  public java.lang.String getDatetime() {
    return datetime;
  }


  /**
   * Sets the value of the 'datetime' field.
   * Time when happiness was made
   * @param value the value to set.
   */
  public void setDatetime(java.lang.String value) {
    this.datetime = value;
  }

  /**
   * Gets the value of the 'level' field.
   * @return How strong this happiness is?
   */
  public int getLevel() {
    return level;
  }


  /**
   * Sets the value of the 'level' field.
   * How strong this happiness is?
   * @param value the value to set.
   */
  public void setLevel(int value) {
    this.level = value;
  }

  /**
   * Creates a new HappinessItem RecordBuilder.
   * @return A new HappinessItem RecordBuilder
   */
  public static com.greatit.demo.avro.HappinessItem.Builder newBuilder() {
    return new com.greatit.demo.avro.HappinessItem.Builder();
  }

  /**
   * Creates a new HappinessItem RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new HappinessItem RecordBuilder
   */
  public static com.greatit.demo.avro.HappinessItem.Builder newBuilder(com.greatit.demo.avro.HappinessItem.Builder other) {
    if (other == null) {
      return new com.greatit.demo.avro.HappinessItem.Builder();
    } else {
      return new com.greatit.demo.avro.HappinessItem.Builder(other);
    }
  }

  /**
   * Creates a new HappinessItem RecordBuilder by copying an existing HappinessItem instance.
   * @param other The existing instance to copy.
   * @return A new HappinessItem RecordBuilder
   */
  public static com.greatit.demo.avro.HappinessItem.Builder newBuilder(com.greatit.demo.avro.HappinessItem other) {
    if (other == null) {
      return new com.greatit.demo.avro.HappinessItem.Builder();
    } else {
      return new com.greatit.demo.avro.HappinessItem.Builder(other);
    }
  }

  /**
   * RecordBuilder for HappinessItem instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<HappinessItem>
    implements org.apache.avro.data.RecordBuilder<HappinessItem> {

    /** Id of a happiness item  */
    private java.lang.String id;
    /** Time when happiness was made */
    private java.lang.String datetime;
    /** How strong this happiness is? */
    private int level;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$, MODEL$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.greatit.demo.avro.HappinessItem.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.datetime)) {
        this.datetime = data().deepCopy(fields()[1].schema(), other.datetime);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
      if (isValidValue(fields()[2], other.level)) {
        this.level = data().deepCopy(fields()[2].schema(), other.level);
        fieldSetFlags()[2] = other.fieldSetFlags()[2];
      }
    }

    /**
     * Creates a Builder by copying an existing HappinessItem instance
     * @param other The existing instance to copy.
     */
    private Builder(com.greatit.demo.avro.HappinessItem other) {
      super(SCHEMA$, MODEL$);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.datetime)) {
        this.datetime = data().deepCopy(fields()[1].schema(), other.datetime);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.level)) {
        this.level = data().deepCopy(fields()[2].schema(), other.level);
        fieldSetFlags()[2] = true;
      }
    }

    /**
      * Gets the value of the 'id' field.
      * Id of a happiness item 
      * @return The value.
      */
    public java.lang.String getId() {
      return id;
    }


    /**
      * Sets the value of the 'id' field.
      * Id of a happiness item 
      * @param value The value of 'id'.
      * @return This builder.
      */
    public com.greatit.demo.avro.HappinessItem.Builder setId(java.lang.String value) {
      validate(fields()[0], value);
      this.id = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'id' field has been set.
      * Id of a happiness item 
      * @return True if the 'id' field has been set, false otherwise.
      */
    public boolean hasId() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'id' field.
      * Id of a happiness item 
      * @return This builder.
      */
    public com.greatit.demo.avro.HappinessItem.Builder clearId() {
      id = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'datetime' field.
      * Time when happiness was made
      * @return The value.
      */
    public java.lang.String getDatetime() {
      return datetime;
    }


    /**
      * Sets the value of the 'datetime' field.
      * Time when happiness was made
      * @param value The value of 'datetime'.
      * @return This builder.
      */
    public com.greatit.demo.avro.HappinessItem.Builder setDatetime(java.lang.String value) {
      validate(fields()[1], value);
      this.datetime = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'datetime' field has been set.
      * Time when happiness was made
      * @return True if the 'datetime' field has been set, false otherwise.
      */
    public boolean hasDatetime() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'datetime' field.
      * Time when happiness was made
      * @return This builder.
      */
    public com.greatit.demo.avro.HappinessItem.Builder clearDatetime() {
      datetime = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'level' field.
      * How strong this happiness is?
      * @return The value.
      */
    public int getLevel() {
      return level;
    }


    /**
      * Sets the value of the 'level' field.
      * How strong this happiness is?
      * @param value The value of 'level'.
      * @return This builder.
      */
    public com.greatit.demo.avro.HappinessItem.Builder setLevel(int value) {
      validate(fields()[2], value);
      this.level = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'level' field has been set.
      * How strong this happiness is?
      * @return True if the 'level' field has been set, false otherwise.
      */
    public boolean hasLevel() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'level' field.
      * How strong this happiness is?
      * @return This builder.
      */
    public com.greatit.demo.avro.HappinessItem.Builder clearLevel() {
      fieldSetFlags()[2] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public HappinessItem build() {
      try {
        HappinessItem record = new HappinessItem();
        record.id = fieldSetFlags()[0] ? this.id : (java.lang.String) defaultValue(fields()[0]);
        record.datetime = fieldSetFlags()[1] ? this.datetime : (java.lang.String) defaultValue(fields()[1]);
        record.level = fieldSetFlags()[2] ? this.level : (java.lang.Integer) defaultValue(fields()[2]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<HappinessItem>
    WRITER$ = (org.apache.avro.io.DatumWriter<HappinessItem>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<HappinessItem>
    READER$ = (org.apache.avro.io.DatumReader<HappinessItem>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    out.writeString(this.id);

    out.writeString(this.datetime);

    out.writeInt(this.level);

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      this.id = in.readString();

      this.datetime = in.readString();

      this.level = in.readInt();

    } else {
      for (int i = 0; i < 3; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.id = in.readString();
          break;

        case 1:
          this.datetime = in.readString();
          break;

        case 2:
          this.level = in.readInt();
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}










