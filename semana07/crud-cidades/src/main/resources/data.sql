-- Semana 07 - Criando usuárias direto no BD
-- Como estamos usando um BD em memória, criamos esse arquivo e nele o código para criar duas usuárias e seus papéis.
-- A senha veio do console, do método printSenhas na classe SecurityConfig

INSERT INTO `usuaria` VALUES (1, 'john', '$2a$10$/.KKJU.G71/Fl.4wKo.lJOfDhxhHPo0o.DPxIy98IuKSaK74WXUy2')
INSERT INTO `usuaria` VALUES (2, 'anna', '$2a$10$/.KKJU.G71/Fl.4wKo.lJOfDhxhHPo0o.DPxIy98IuKSaK74WXUy2')

INSERT INTO `usuaria_papeis` VALUES (1, 'listar')
INSERT INTO `usuaria_papeis` VALUES (2, 'listar')
INSERT INTO `usuaria_papeis` VALUES (2, 'admin')

-- Outra coisa que foi feita, foi adicionar (spring.jpa.defer-datasource-initialization=true) no arquivo application.properties
-- Dessa forma, esse arquivo aqui (data.sql) será executado antes da aplicação entrar em ação, garantindo que as usuárias já existam
-- quando a aplicação tentar autenticar