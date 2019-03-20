CREATE TABLE `planeta` (
  `id` bigint(20) NOT NULL,
  `nome` varchar(100) NOT NULL,
  `clima` varchar(50) NOT NULL,
  `terreno` varchar(50) NOT NULL,
  `data_atualizacao` datetime NULL,
  `data_criacao` datetime NOT NULL,
  `aparicao` INT NULL,
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indexes for table `planeta`
--
ALTER TABLE `planeta`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for table `planeta`
--
ALTER TABLE `planeta`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

  