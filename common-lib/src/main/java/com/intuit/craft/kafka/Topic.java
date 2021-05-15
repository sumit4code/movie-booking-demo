package com.intuit.craft.kafka;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Topic {

    private String name;
    private int partitions;
    private int replicas;
}