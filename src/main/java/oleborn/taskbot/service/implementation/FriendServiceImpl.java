package oleborn.taskbot.service.implementation;

import jakarta.annotation.Resource;
import oleborn.taskbot.mapper.FriendMapper;
import oleborn.taskbot.model.dto.FriendDto;
import oleborn.taskbot.model.entities.Friend;
import oleborn.taskbot.model.entities.Task;
import oleborn.taskbot.repository.FriendRepository;
import oleborn.taskbot.service.interfaces.FriendService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FriendServiceImpl implements FriendService {

    @Resource
    private FriendRepository friendRepository;

    @Resource
    private FriendMapper friendMapper;


    @Override
    public FriendDto createFriend(FriendDto friendDto) {
        Friend friendEntity = friendMapper.fromFriendDto(friendDto);
        return friendMapper.toFriendDto(friendRepository.save(friendEntity));
    }

    @Override
    public FriendDto updateFriend(FriendDto friendDto) {
        Friend friendEntity = friendRepository.findById(friendDto.getId())
                .orElseThrow(() -> new RuntimeException("ПОКА ТЕСТ"));

        friendMapper.updateFriendEntityFromDto(friendDto, friendEntity);

        return friendMapper.toFriendDto(friendRepository.save(friendEntity));
    }

    @Override
    public void deleteFriend(FriendDto friendDto) {
        friendRepository.deleteById(friendDto.getId());
    }

    @Override
    public FriendDto getFriendByID(Long id) {
        Optional<Friend> friendEntity = friendRepository.findById(id);

        return friendEntity.map(friendMapper::toFriendDto)
                .orElseThrow(() -> new RuntimeException("ПОКА ТЕСТ"));
    }
}
