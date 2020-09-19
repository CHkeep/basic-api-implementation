package com.thoughtworks.rslist.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "rsEvent")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RsEventPO {
    @Id
    @GeneratedValue
    private int id;
    @Column(name = "name")
    private String eventName;
    private String keyWords;
    private  int voteNum = 0;
    @ManyToOne
    private UserPO userPO;

}


