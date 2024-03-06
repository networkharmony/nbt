# Named Binary Tag (NBT) Implementation in Kotlin

## Introduction

The following presents an implementation of Named Binary Tag (NBT), drawing inspiration from Minecraft and developed using the Kotlin programming language.

## Features

- **Support for Varied Data Types**: Encompassing both primitive and complex data types.
  
- **Custom Type Creation**: Define and integrate custom data types seamlessly.
  
- **Optimized Memory Utilization**: Minimizing memory footprint for efficient resource usage.
  
- **Compact Design**: A highly condensed structure, facilitating streamlined storage.

## Minecraft NBT Overview

The NBT system within Minecraft's is characterized by its simplicity, albeit lacking in optimization. Minecraft offers various NBT types, each serving distinct purposes:

1. **Empty (NBT TAG END)**
2. **Byte (NBT TAG BYTE)**
3. **Short (NBT TAG SHORT)**
4. **Int (NBT TAG INT)**
5. **Long (NBT TAG LONG)**
6. **Float (NBT TAG FLOAT)**
7. **Double (NBT TAG DOUBLE)**
8. **ByteArray (NBT TAG BYTEARRAY)**
9. **String (NBT TAG STRING)**
10. **List (NBT TAG LIST)**
11. **Compound (NBT TAG COMPOUND)**
12. **IntArray (NBT TAG INTARRAY)**
13. **LongArray (NBT TAG LONGARRAY) - Introduced in version 1.13+**

## Extended NBT Types

In addition to the robust set provided by Minecraft, this implementation introduces eight supplementary NBT types, enhancing the versatility of the system:

1. **Char**: Introducing a simple primitive char type.
2. **Boolean**: Departing from Minecraft's approach, utilizing 1 byte for 8 boolean values through a bit-based strategy, optimizing storage efficiency.
3. **Set**: Akin to a List but tailored to exclude duplicates.
4. **ShortArray**: A straightforward primitive short array type.
5. **FloatArray**: Embracing a rudimentary primitive float array type.
6. **DoubleArray**: Enabling a basic primitive double array type.
7. **CharArray**: Incorporating a basic primitive char array type.
8. **BooleanArray**: Adhering to a primitive boolean array type, storing boolean values as 1 boolean per byte.
9. **PackedBooleanArray**: Presenting a primitive boolean array type with a storage approach of 8 booleans per byte.

## Advantages Over Minecraft NBT Implementation

### Inline Classes

This NBT implementation leverages inline classes, ensuring minimal memory allocation by design, only resorting to allocation when deemed necessary.

### Buffered Implementation

Optimization is achieved through buffering implementations to ByteArray. This not only enhances overall performance but also synergizes with compression algorithms, culminating in a more efficient and compact representation.

### FastUtil
Usage of fastutil collection types for maximum performance

## In-Depth Exploration of New Types

### Char

A fundamental primitive char type, providing a concise representation.

### Boolean

A departure from conventional NBT boolean storage, leveraging bits for an impressive optimization that allows storing 8 boolean values in just 1 byte.

### Set

A tailored data structure akin to List but distinguished by its exclusion of duplicate elements, fostering data uniqueness.

### ShortArray

A straightforward representation of a primitive short array type, catering to specific numerical requirements.

### FloatArray

A rudimentary yet powerful representation of a primitive float array type, accommodating decimal precision.

### DoubleArray

A basic implementation of a primitive double array type, addressing scenarios requiring higher precision.

### CharArray

An incorporation of a primitive char array type, catering to textual data needs within the NBT framework.

### BooleanArray

A streamlined representation of a primitive boolean array type, allocating 1 byte for each boolean value.

### PackedBooleanArray

An innovative approach to boolean array storage, utilizing a compact representation with 8 booleans stored per byte.

## Usage
Simple overall usage
```kt
  val compound = compound {
    set("Name", "Test")
    set("Version", "1.12")
    set("Patch", 7)
    // multiple booleans
    set("Available", true, false, false)
    set("Authors", tagList {
      // will be improved in future versions
      add("uin".toTag())
      add("other".toTag())
    })
  }
  
  // write
  TagIO.write(stream, compound)
  TagIO.write(file, compound)
  
  // read
  val loaded = TagIO.read(stream)
  val loaded = TagIO.read(file)
  
  val name = compound.string("Name")
  val version = compound.string("Version", default = "1.0")
  val authors = compound.stringList("Authors")

  // getting multiple booleans
  val available = compound.booleanTag("Available")
  val availableForWindows = available[0]
  val availableForLinux = available[1]
  val availableForMac = available[2]
```

## Creating a custom type
In this example we gonna create a UUID NBT type!
```kt
// don't forget to set as inline class!
@JvmInline
value class UUIDTag(val value: UUID) : Tag {
  
  // this allows we get any value in the tag if we don't know the type
  override val genericValue get() = value
  
  // the type of this tag
  override val type get() = Type
  
  // the write function is used to save tag value to data output
  override fun write(data: DataOutput) {
    data.writeLong(value.mostSignificantBits)
    data.writeLong(value.leastSignificantBits)
  }
  
  // creates a copy of the tag, in some cases this can be useful
  // but not for this example
  override fun copy(): UUIDTag = UUIDTag(value)
  
  override fun toString() = value.toString()
  
  // we also need to create a type to loading them back.
  object Type : TagType<UUIDTag>() {
    override fun load(data: DataInput): UUIDTag {
      return UUIDTag(UUID(data.readLong(), data.readLong()))
    }
  }
}

// registering the type
TagTypes.registerType(UUIDTag.Type)
```
