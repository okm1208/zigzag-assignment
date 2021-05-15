package com.okm1208.vacation.common.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-14
 */

@Table(name="account")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountNo;

    @Column(length = 30, nullable = false, unique = true)
    private String accountId;

    @Column(length = 200, nullable = false)
    private String password;

    @Column(length = 50,nullable = false)
    private boolean active;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="account_authorities" ,joinColumns = @JoinColumn(name = "accountNo") )
    @Enumerated(EnumType.STRING)
    @Column(name = "authority")
    private List<AccountAuthority> roles;

    @OneToOne(mappedBy = "account")
    @PrimaryKeyJoinColumn
    private VacationInfo vacationInfo;

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public enum AccountStatus {
        ACTIVE("활성"),
        LOCKED("잠김"),
        TERMINATE("탈퇴");

        final String desc;
    }
    public enum AccountAuthority implements GrantedAuthority {
        ROLE_ADMIN, ROLE_USER;

        @Override
        public String getAuthority() {
            return this.name();
        }

        @Override
        public String toString(){
            return this.name();
        }

        public static AccountAuthority findByName(String name){
            for(AccountAuthority v : values()){
                if( v.name().equals(name)){
                    return v;
                }
            }
            return null;
        }
    }
}
