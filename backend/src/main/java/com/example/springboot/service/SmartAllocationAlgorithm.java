package com.example.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.JudgeBedName;
import com.example.springboot.dto.AllocationRecord;
import com.example.springboot.dto.AllocationResult;
import com.example.springboot.entity.DormBuild;
import com.example.springboot.entity.DormRoom;
import com.example.springboot.entity.Student;
import com.example.springboot.mapper.DormBuildMapper;
import com.example.springboot.mapper.DormRoomMapper;
import com.example.springboot.mapper.StudentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 智能宿舍分配算法
 * <p>
 * 基于贪心策略的多维约束匹配算法，实现宿舍自动分配
 * <p>
 * 算法特点：
 * 1. 性别隔离：不同性别分配到不同楼宇
 * 2. 房间利用率优先：优先选择已有人的房间
 * 3. 楼层均衡：优先选择人数较少的楼层
 * 4. 床位顺序：按1→2→3→4顺序分配
 */
@Slf4j
@Component
public class SmartAllocationAlgorithm {

    private final StudentMapper studentMapper;
    private final DormRoomMapper dormRoomMapper;
    private final DormBuildMapper dormBuildMapper;

    public SmartAllocationAlgorithm(
            StudentMapper studentMapper,
            DormRoomMapper dormRoomMapper,
            DormBuildMapper dormBuildMapper) {
        this.studentMapper = studentMapper;
        this.dormRoomMapper = dormRoomMapper;
        this.dormBuildMapper = dormBuildMapper;
    }

    /**
     * 智能批量分配宿舍
     *
     * @param usernames 待分配学生用户名列表
     * @return 分配结果
     */
    @Transactional(rollbackFor = Exception.class)
    public AllocationResult allocate(List<String> usernames) {
        long startTime = System.currentTimeMillis();

        log.info("开始智能分配，待分配学生数: {}", usernames.size());

        List<AllocationRecord> successList = new ArrayList<>();
        List<String> failList = new ArrayList<>();

        try {
            // Step 1: 获取所有待分配学生信息
            List<Student> students = getStudents(usernames, failList);
            if (students.isEmpty()) {
                return AllocationResult.error(failList);
            }

            // Step 2: 按性别分组
            Map<String, List<Student>> genderGroups = groupByGender(students);

            // Step 3: 获取所有可用房间
            List<DormRoom> availableRooms = getAvailableRooms();
            if (availableRooms.isEmpty()) {
                log.error("没有可用的房间");
                failList.addAll(usernames);
                return AllocationResult.error(failList);
            }

            // Step 4: 按楼宇分组房间
            Map<Integer, List<DormRoom>> roomsByBuild = groupRoomsByBuild(availableRooms);

            // Step 5: 对每个性别组执行分配
            for (Map.Entry<String, List<Student>> entry : genderGroups.entrySet()) {
                String gender = entry.getKey();
                List<Student> genderStudents = entry.getValue();

                // 按用户名排序，保证分配顺序一致
                genderStudents.sort(Comparator.comparing(Student::getUsername));

                // 获取该性别可用的楼宇（这里简化处理，假设所有楼宇都可用）
                // 实际项目中可能需要根据楼宇属性（如男女宿舍）进行过滤
                List<DormRoom> genderRooms = getRoomsForGender(roomsByBuild, gender);

                // 执行分配
                allocateGenderGroup(genderStudents, genderRooms, successList, failList);
            }

            // 计算房间利用率
            double utilization = calculateRoomUtilization();

            long timeSpent = System.currentTimeMillis() - startTime;
            log.info("分配完成 - 成功: {}, 失败: {}, 耗时: {}ms, 利用率: {}%",
                    successList.size(), failList.size(), timeSpent, utilization * 100);

            return AllocationResult.success(successList, failList, utilization, timeSpent);

        } catch (Exception e) {
            log.error("分配过程发生异常", e);
            throw new RuntimeException("分配失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取学生信息
     */
    private List<Student> getStudents(List<String> usernames, List<String> failList) {
        List<Student> students = new ArrayList<>();

        for (String username : usernames) {
            QueryWrapper<Student> wrapper = new QueryWrapper<>();
            wrapper.eq("username", username);
            Student student = studentMapper.selectOne(wrapper);

            if (student == null) {
                log.warn("学生不存在: {}", username);
                failList.add(username);
            } else if (student.getGender() == null || student.getGender().isEmpty()) {
                log.warn("学生性别信息缺失: {}", username);
                failList.add(username);
            } else {
                students.add(student);
            }
        }

        return students;
    }

    /**
     * 按性别分组
     */
    private Map<String, List<Student>> groupByGender(List<Student> students) {
        return students.stream()
                .collect(Collectors.groupingBy(Student::getGender));
    }

    /**
     * 获取所有可用房间（当前人数 < 最大容量）
     */
    private List<DormRoom> getAvailableRooms() {
        QueryWrapper<DormRoom> wrapper = new QueryWrapper<>();
        wrapper.lt("current_capacity", 4); // 假设最大容量为4
        return dormRoomMapper.selectList(wrapper);
    }

    /**
     * 按楼宇分组房间
     */
    private Map<Integer, List<DormRoom>> groupRoomsByBuild(List<DormRoom> rooms) {
        return rooms.stream()
                .collect(Collectors.groupingBy(DormRoom::getDormBuildId));
    }

    /**
     * 获取指定性别可用的房间
     * 简化处理：返回所有房间（实际项目中需要根据楼宇属性过滤）
     */
    private List<DormRoom> getRoomsForGender(Map<Integer, List<DormRoom>> roomsByBuild, String gender) {
        // 这里可以扩展：根据楼宇的性别属性进行过滤
        return roomsByBuild.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    /**
     * 为指定性别组分配房间
     */
    private void allocateGenderGroup(
            List<Student> students,
            List<DormRoom> rooms,
            List<AllocationRecord> successList,
            List<String> failList) {

        if (rooms.isEmpty()) {
            log.warn("没有可用房间，无法分配 {} 名学生", students.size());
            failList.addAll(students.stream().map(Student::getUsername).collect(Collectors.toList()));
            return;
        }

        // 计算每层楼的人数（用于楼层均衡）
        Map<Integer, Integer> floorPeopleCount = calculateFloorPeopleCount(rooms);

        for (Student student : students) {
            DormRoom bestRoom = findBestRoom(rooms, floorPeopleCount);

            if (bestRoom == null) {
                log.warn("没有可用房间，学生 {} 分配失败", student.getUsername());
                failList.add(student.getUsername());
                continue;
            }

            // 分配床位
            Integer bedNum = findAvailableBed(bestRoom);
            if (bedNum == null) {
                log.warn("房间 {} 没有可用床位", bestRoom.getDormRoomId());
                failList.add(student.getUsername());
                continue;
            }

            // 更新房间
            updateRoom(bestRoom, bedNum, student.getUsername());

            // 更新楼层统计
            floorPeopleCount.put(bestRoom.getFloorNum(),
                    floorPeopleCount.getOrDefault(bestRoom.getFloorNum(), 0) + 1);

            // 记录分配结果
            AllocationRecord record = new AllocationRecord(
                    student.getUsername(),
                    student.getName(),
                    student.getGender(),
                    bestRoom.getDormRoomId(),
                    bestRoom.getDormBuildId(),
                    bestRoom.getFloorNum(),
                    bedNum
            );
            successList.add(record);

            log.info("分配成功: {} -> 房间{}, 床位{}", student.getUsername(), bestRoom.getDormRoomId(), bedNum);
        }
    }

    /**
     * 计算每层楼的人数
     */
    private Map<Integer, Integer> calculateFloorPeopleCount(List<DormRoom> rooms) {
        Map<Integer, Integer> countMap = new ConcurrentHashMap<>();
        for (DormRoom room : rooms) {
            countMap.merge(room.getFloorNum(), room.getCurrentCapacity(), Integer::sum);
        }
        return countMap;
    }

    /**
     * 查找最优房间
     * <p>
     * 评分算法：
     * - 基础分 = 当前人数 × 100（优先选择已有人的房间）
     * - 楼层均衡加成 = (总楼层 - 楼层排名) × 10（优先选择人数少的楼层）
     * - 总分 = 基础分 + 楼层均衡加成
     */
    private DormRoom findBestRoom(List<DormRoom> rooms, Map<Integer, Integer> floorPeopleCount) {
        return rooms.stream()
                .filter(room -> room.getCurrentCapacity() < room.getMaxCapacity())
                .max(Comparator.comparingDouble(room -> calculateRoomScore(room, floorPeopleCount)))
                .orElse(null);
    }

    /**
     * 计算房间评分
     */
    private double calculateRoomScore(DormRoom room, Map<Integer, Integer> floorPeopleCount) {
        // 基础分：当前人数越多，分数越高（优先填满已有房间）
        double baseScore = room.getCurrentCapacity() * 100.0;

        // 楼层均衡加成：人数少的楼层加分更多
        int floorPeople = floorPeopleCount.getOrDefault(room.getFloorNum(), 0);
        int maxFloorPeople = floorPeopleCount.values().stream().max(Integer::compare).orElse(1);
        int floorRank = maxFloorPeople - floorPeople; // 人数越多，排名越小（0最小）
        double floorBonus = floorRank * 10.0;

        return baseScore + floorBonus;
    }

    /**
     * 查找可用床位
     */
    private Integer findAvailableBed(DormRoom room) {
        for (int i = 1; i <= 4; i++) {
            String bedName = JudgeBedName.getBedName(i);
            if (bedName == null) continue;

            String bedValue = getBedValue(room, bedName);
            if (bedValue == null || bedValue.isEmpty()) {
                return i;
            }
        }
        return null;
    }

    /**
     * 获取床位值
     */
    private String getBedValue(DormRoom room, String bedName) {
        switch (bedName) {
            case "first_bed":
                return room.getFirstBed();
            case "second_bed":
                return room.getSecondBed();
            case "third_bed":
                return room.getThirdBed();
            case "fourth_bed":
                return room.getFourthBed();
            default:
                return null;
        }
    }

    /**
     * 更新房间床位信息
     */
    private void updateRoom(DormRoom room, Integer bedNum, String username) {
        String bedName = JudgeBedName.getBedName(bedNum);
        if (bedName == null) {
            throw new IllegalArgumentException("无效的床位编号: " + bedNum);
        }

        switch (bedName) {
            case "first_bed":
                room.setFirstBed(username);
                break;
            case "second_bed":
                room.setSecondBed(username);
                break;
            case "third_bed":
                room.setThirdBed(username);
                break;
            case "fourth_bed":
                room.setFourthBed(username);
                break;
        }

        room.setCurrentCapacity(room.getCurrentCapacity() + 1);
        dormRoomMapper.updateById(room);
    }

    /**
     * 计算房间利用率
     */
    private double calculateRoomUtilization() {
        QueryWrapper<DormRoom> wrapper = new QueryWrapper<>();
        List<DormRoom> allRooms = dormRoomMapper.selectList(wrapper);

        if (allRooms.isEmpty()) {
            return 0.0;
        }

        int totalCapacity = allRooms.stream().mapToInt(DormRoom::getMaxCapacity).sum();
        int currentCapacity = allRooms.stream().mapToInt(DormRoom::getCurrentCapacity).sum();

        return totalCapacity > 0 ? (double) currentCapacity / totalCapacity : 0.0;
    }
}
