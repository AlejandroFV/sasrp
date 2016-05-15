-- phpMyAdmin SQL Dump
-- version 4.1.14
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: May 15, 2016 at 11:22 PM
-- Server version: 5.6.17
-- PHP Version: 5.5.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `theater`
--
CREATE DATABASE IF NOT EXISTS `theater` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `theater`;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_reservation`
--

CREATE TABLE IF NOT EXISTS `tbl_reservation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `seat_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`),
  UNIQUE KEY `seat_id` (`seat_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_seat`
--

CREATE TABLE IF NOT EXISTS `tbl_seat` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(3) NOT NULL,
  `status` enum('available','in-process','reserved') NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=101 ;

--
-- Dumping data for table `tbl_seat`
--

INSERT INTO `tbl_seat` (`id`, `name`, `status`) VALUES
(1, 'A1', 'available'),
(2, 'A2', 'available'),
(3, 'A3', 'available'),
(4, 'A4', 'available'),
(5, 'A5', 'available'),
(6, 'A6', 'available'),
(7, 'A7', 'available'),
(8, 'A8', 'available'),
(9, 'A9', 'available'),
(10, 'A10', 'available'),
(11, 'B1', 'available'),
(12, 'B2', 'available'),
(13, 'B3', 'available'),
(14, 'B4', 'available'),
(15, 'B5', 'available'),
(16, 'B6', 'available'),
(17, 'B7', 'available'),
(18, 'B8', 'available'),
(19, 'B9', 'available'),
(20, 'B10', 'available'),
(21, 'C1', 'available'),
(22, 'C2', 'available'),
(23, 'C3', 'available'),
(24, 'C4', 'available'),
(25, 'C5', 'available'),
(26, 'C6', 'available'),
(27, 'C7', 'available'),
(28, 'C8', 'available'),
(29, 'C9', 'available'),
(30, 'C10', 'available'),
(31, 'D1', 'available'),
(32, 'D2', 'available'),
(33, 'D3', 'available'),
(34, 'D4', 'available'),
(35, 'D5', 'available'),
(36, 'D6', 'available'),
(37, 'D7', 'available'),
(38, 'D8', 'available'),
(39, 'D9', 'available'),
(40, 'D10', 'available'),
(41, 'E1', 'available'),
(42, 'E2', 'available'),
(43, 'E3', 'available'),
(44, 'E4', 'available'),
(45, 'E5', 'available'),
(46, 'E6', 'available'),
(47, 'E7', 'available'),
(48, 'E8', 'available'),
(49, 'E9', 'available'),
(50, 'E10', 'available'),
(51, 'F1', 'available'),
(52, 'F2', 'available'),
(53, 'F3', 'available'),
(54, 'F4', 'available'),
(55, 'F5', 'available'),
(56, 'F6', 'available'),
(57, 'F7', 'available'),
(58, 'F8', 'available'),
(59, 'F9', 'available'),
(60, 'F10', 'available'),
(61, 'G1', 'available'),
(62, 'G2', 'available'),
(63, 'G3', 'available'),
(64, 'G4', 'available'),
(65, 'G5', 'available'),
(66, 'G6', 'available'),
(67, 'G7', 'available'),
(68, 'G8', 'available'),
(69, 'G9', 'available'),
(70, 'G10', 'available'),
(71, 'H1', 'available'),
(72, 'H2', 'available'),
(73, 'H3', 'available'),
(74, 'H4', 'available'),
(75, 'H5', 'available'),
(76, 'H6', 'available'),
(77, 'H7', 'available'),
(78, 'H8', 'available'),
(79, 'H9', 'available'),
(80, 'H10', 'available'),
(81, 'I1', 'available'),
(82, 'I2', 'available'),
(83, 'I3', 'available'),
(84, 'I4', 'available'),
(85, 'I5', 'available'),
(86, 'I6', 'available'),
(87, 'I7', 'available'),
(88, 'I8', 'available'),
(89, 'I9', 'available'),
(90, 'I10', 'available'),
(91, 'J1', 'available'),
(92, 'J2', 'available'),
(93, 'J3', 'available'),
(94, 'J4', 'available'),
(95, 'J5', 'available'),
(96, 'J6', 'available'),
(97, 'J7', 'available'),
(98, 'J8', 'available'),
(99, 'J9', 'available'),
(100, 'J10', 'available');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_user`
--

CREATE TABLE IF NOT EXISTS `tbl_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `tbl_user`
--

INSERT INTO `tbl_user` (`id`, `first_name`, `last_name`) VALUES
(1, 'John', 'Doe');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `tbl_reservation`
--
ALTER TABLE `tbl_reservation`
  ADD CONSTRAINT `foreign_key_reservation_seat` FOREIGN KEY (`seat_id`) REFERENCES `tbl_seat` (`id`),
  ADD CONSTRAINT `foreign_key_reservation_user` FOREIGN KEY (`user_id`) REFERENCES `tbl_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
