package com.shalimov.movieland.dao.jpa;

import com.shalimov.movieland.dao.UserDao;
import com.shalimov.movieland.dao.jpa.dto.UserDto;
import com.shalimov.movieland.entity.User;
import com.shalimov.movieland.entity.UserType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class JpaUserDao implements UserDao {


    private  final SessionFactory sessionFactory;
    @Autowired
    public JpaUserDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public Optional<User> getByEmail(String email) {
        Query<UserDto> userDtoQuery = getCurrentSession().createQuery("select u from UserDto u  where u.email=:email", UserDto.class);
        userDtoQuery.setParameter("email", email);
        List<UserDto> moviesDto = userDtoQuery.getResultList();
        return Optional.ofNullable(userDtoConverter(moviesDto).get(0));
    }

    @Override
    public List<User> getTopUsers() {
        Query<UserDto> userDtoQuery = getCurrentSession().createQuery("select m from UserDto m  order by m.averageRating", UserDto.class);
        List<UserDto> usersDto = userDtoQuery.setMaxResults(3).getResultList();
        return userDtoConverter(usersDto);
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    private List<User> userDtoConverter(List<UserDto> usersDto) {
        List<User> users = new ArrayList<>();
        for (UserDto userDto:usersDto) {
           User user =new User();
            user.setId(userDto.getId());
            user.setNickName(userDto.getFirstName()+userDto.getLastName());
            user.setEmail(userDto.getEmail());
            user.setUserType(UserType.getTypeById(userDto.getUserType()));
            user.setPassword(userDto.getPassword());
            user.setSalt(userDto.getSalt());

            users.add(user);
        }
        return users;
    }
}
