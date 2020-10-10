package com.thoughtworks.rslist.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "user")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPO {
    @Id
    @GeneratedValue(generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private int id;
    @Column(name = "name")
    private String userName;
    private String gender;
    private int age;
    private String email;
    private String phone;
    @Builder.Default
    private int voteNum = 10;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "userPO")
    private List<RsEventPO> rsEventPOs;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user")
    private List<VotePO> votePOS;
}
