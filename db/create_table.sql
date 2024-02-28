-- 创建库
create database if not exists career;

-- 切换库
use career;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    userPhone    varchar(512)                           null comment '手机',
    email        varchar(512)                           null comment '邮箱',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userRole     varchar(256) default 'employee'        not null comment '用户角色：employee/employer/admin/ban',
    lastActive   int          default 0                 not null comment '近期活跃天数',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    index idx_userAccount (userAccount)
) comment '用户' collate = utf8mb4_unicode_ci;

-- 应聘者表
create table if not exists employee
(
    id               bigint auto_increment comment 'id' primary key,
    userId           bigint                             not null comment '用户id',
    gender           tinyint  default 0                 not null comment '性别（0 - 女 1 - 男）',
    age              int      default 20                not null comment '年龄',
    advantage        text                               null comment '个人优势',
    qualificationIds varchar(1024)                      null comment '资格证书',
    skillTag         varchar(512)                       not null comment '技能标签',
    education        int      default 1                 not null comment '最高学历',
    graduateYear     int                                not null comment '毕业年份',
    jobStatus        tinyint                            not null comment '求职状态',
    biography        varchar(1024)                      null comment '简历地址',
    cityId           bigint                             not null comment '居住地',
    createTime       datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime       datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete         tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '应聘者' collate = utf8mb4_unicode_ci;

-- 应聘者经历表
create table if not exists employee_experience
(
    id                 bigint auto_increment comment 'id' primary key,
    userId             bigint                             not null comment '用户id',
    experienceName     varchar(256)                       not null comment '经历名称',
    beginTime          varchar(256)                       not null comment '开始时间',
    endTime            varchar(256)                       not null comment '结束时间',
    jobRole            varchar(256)                       not null comment '担任职务',
    experienceDescript varchar(512)                       not null comment '经历描述',
    experienceType     tinyint                            not null comment '经历种类',
    createTime         datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime         datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete           tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '应聘者经历';

-- 专业表
create table if not exists major
(
    id         bigint auto_increment comment 'id' primary key,
    majorName  varchar(256)                       not null comment '专业名称',
    postNum    int      default 0                 not null comment '相关数量',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除'
) comment '专业' collate = utf8mb4_unicode_ci;

-- 学校表
create table if not exists school
(
    id         bigint auto_increment comment 'id' primary key,
    schoolName varchar(256)                       not null comment '专业名称',
    postNum    int      default 0                 not null comment '相关数量',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除'
) comment '学校' collate = utf8mb4_unicode_ci;

-- 行业表
create table if not exists industry
(
    id           bigint auto_increment comment 'id' primary key,
    industryName varchar(256)                       not null comment '行业名称',
    industryType int                                not null comment '行业类型',
    postNum      int      default 0                 not null comment '相关数量',
    createTime   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint  default 0                 not null comment '是否删除'
) comment '行业' collate = utf8mb4_unicode_ci;

-- 行业类型表
create table if not exists industry_type
(
    id               bigint auto_increment comment 'id' primary key,
    industryTypeName varchar(256)                       not null comment '行业类型名称',
    postNum          int      default 0                 not null comment '相关数量',
    createTime       datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime       datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete         tinyint  default 0                 not null comment '是否删除'
) comment '行业类型' collate = utf8mb4_unicode_ci;

-- 教育经历表
create table if not exists education_experience
(
    id            bigint auto_increment comment 'id' primary key,
    userId        bigint                             not null comment '用户id',
    schoolId      bigint                             not null comment '学校id',
    educationType tinyint                            not null comment '学历类型',
    beginYear     int                                not null comment '开始年份',
    endYear       int                                not null comment '结束年份',
    majorId       bigint                             not null comment '专业id',
    activity      text                               null comment '在校经历',
    postNum       int      default 0                 not null comment '相关数量',
    createTime    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime    datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete      tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '教育经历' collate = utf8mb4_unicode_ci;

-- 资格证书表
create table if not exists qualification
(
    id                bigint auto_increment comment 'id' primary key,
    qualificationName varchar(256)                       not null comment '资格证书名称',
    qualificationType int                                not null comment '资格证书类型',
    createTime        datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime        datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete          tinyint  default 0                 not null comment '是否删除'
) comment '资格证书' collate = utf8mb4_unicode_ci;

-- 职业表
create table if not exists position
(
    id               bigint auto_increment comment 'id' primary key,
    positionName     varchar(256)                       not null comment '职业名称',
    positionDescript varchar(1024)                      null comment '职业介绍',
    positionType     int                                not null comment '职业类型',
    postNum          int      default 0                 not null comment '相关数量',
    createTime       datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime       datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete         tinyint  default 0                 not null comment '是否删除'
) comment '职业' collate = utf8mb4_unicode_ci;

-- 职业类型表
create table if not exists position_type
(
    id               bigint auto_increment comment 'id' primary key,
    positionTypeName varchar(256)                       not null comment '职业类型名称',
    postNum          int      default 0                 not null comment '相关数量',
    createTime       datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime       datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete         tinyint  default 0                 not null comment '是否删除'
) comment '职业类型表' collate = utf8mb4_unicode_ci;

-- 应聘者期望岗位
create table if not exists employee_wish_career
(
    id                bigint auto_increment comment 'id' primary key,
    userId            bigint                                 not null comment '用户id',
    positionId        bigint                                 not null comment '职业id',
    industryId        bigint       default 0                 not null comment '行业id',
    salaryExpectation varchar(256) default '-'               not null comment '薪水要求（例如：10-15）',
    cityId            bigint                                 not null comment '工作城市',
    createTime        datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime        datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete          tinyint      default 0                 not null comment '是否删除'
);

-- 城市表
create table if not exists city
(
    id           bigint auto_increment comment 'id' primary key,
    cityName     varchar(128)                       not null comment '城市名称',
    provinceType int                                not null comment '省份类型',
    postNum      int      default 0                 not null comment '相关数量',
    createTime   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint  default 0                 not null comment '是否删除'
) comment '城市' collate = utf8mb4_unicode_ci;;

-- 省份类型
create table if not exists province_type
(
    id           bigint auto_increment comment 'id' primary key,
    provinceName varchar(128)                       not null comment '省份名称',
    createTime   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint  default 0                 not null comment '是否删除'
) comment '省份类型' collate = utf8mb4_unicode_ci;

-- 招聘者表
create table if not exists employer
(
    id         bigint auto_increment comment 'id' primary key,
    userId     bigint                             not null comment '用户id',
    companyId  bigint                             not null comment '公司id',
    positionId bigint                             not null comment '招聘者职位id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '招聘者' collate = utf8mb4_unicode_ci;

-- 公司信息表
create table if not exists company
(
    id              bigint auto_increment comment 'id' primary key,
    companyName     varchar(256)                            not null comment '公司名称',
    companyDescript text                                    not null comment '公司介绍',
    companyAddress  varchar(1024)                           not null comment '公司地点',
    cityId          bigint                                  not null comment '所在城市id',
    postNum         int           default 0                 not null comment '相关数量',
    companyLogo     varchar(256)                            not null comment '公司Logo',
    companyImg      varchar(1024) default '[]'              null comment '公司图片',
    companyIndustry bigint                                  not null comment '公司产业',
    createTime      datetime      default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime      datetime      default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete        tinyint       default 0                 not null comment '是否删除'
) comment '公司信息' collate = utf8mb4_unicode_ci;

-- 招聘信息表
create table if not exists recruitment
(
    id              bigint auto_increment comment 'id' primary key,
    jobName        varchar(512)                            not null comment '岗位招聘标题',
    userId          bigint                                  not null comment '岗位发布者id',
    companyId       bigint                                  not null comment '公司id',
    positionId      bigint                                  not null comment '职业id',
    industryId      bigint                                  not null comment '行业id',
    jobDescription  text                                    not null comment '职位详情',
    jobRequirements text                                    not null comment '职位要求',
    educationType   tinyint                                 null comment '最低学历要求',
    jobKeyword      varchar(1024)                           not null comment '职业关键词JSON',
    jobSkills       varchar(1024) default '[]'              null comment '职业技术栈JSON',
    jobType         int                                     not null comment '职业类型（实习、兼职、春招等）',
    jobAddress      varchar(256)                            not null comment '岗位要求地址',
    salaryUpper     int                                     null comment '薪水上限',
    salaryLower     int                                     null comment '薪水下限',
    salaryUnit      tinyint                                 null comment '薪水种类',
    cityId          bigint                                  not null comment '所在城市id',
    jobActive       tinyint       default 0                 not null comment '招聘活跃 （0 - 招聘中 1 - 结束招聘）',
    createTime      datetime      default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime      datetime      default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete        tinyint       default 0                 not null comment '是否删除',
    index idx_userId (userId),
    index idx_companyId (companyId)
) comment '招聘信息' collate = utf8mb4_unicode_ci;