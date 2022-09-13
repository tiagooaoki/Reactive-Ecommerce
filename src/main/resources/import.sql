INSERT INTO dados_pessoais(id,nome, dataNascimento, cpf) VALUES (nextval('hibernate_sequence'), 'nome sobrenome', '2000-01-01', '12345678901');
INSERT INTO usuario(id,username, password, email, dadosPessoais_id) VALUES (nextval('hibernate_sequence'), 'username', '1234', 'username@provedor.com', 1);
INSERT INTO produto(id, nome, tipo, descricao, preco) VALUES (nextval('hibernate_sequence'), 'Livro - Ressurreição ','Livro','Autor: Machado de Assis, ano: 1872',11);
INSERT INTO produto(id, nome, tipo, descricao, preco) VALUES (nextval('hibernate_sequence'), 'Livro - A mão e a luva ','Livro','Autor: Machado de Assis, ano: 1874',84);
INSERT INTO produto(id, nome, tipo, descricao, preco) VALUES (nextval('hibernate_sequence'), 'Livro - Helena ','Livro','Autor: Machado de Assis, ano: 1876',17);
INSERT INTO produto(id, nome, tipo, descricao, preco) VALUES (nextval('hibernate_sequence'), 'Livro - Iaiá Garcia ','Livro','Autor: Machado de Assis, ano: 1878',68);
INSERT INTO produto(id, nome, tipo, descricao, preco) VALUES (nextval('hibernate_sequence'), 'Livro - Memórias Póstumas de Brás Cubas ','Livro','Autor: Machado de Assis, ano: 1881',86);
INSERT INTO produto(id, nome, tipo, descricao, preco) VALUES (nextval('hibernate_sequence'), 'Livro - Casa Velha ','Livro','Autor: Machado de Assis, ano: 1885',82);
INSERT INTO produto(id, nome, tipo, descricao, preco) VALUES (nextval('hibernate_sequence'), 'Livro - Quincas Borba ','Livro','Autor: Machado de Assis, ano: 1891',70);
INSERT INTO produto(id, nome, tipo, descricao, preco) VALUES (nextval('hibernate_sequence'), 'Livro - Dom Casmurro ','Livro','Autor: Machado de Assis, ano: 1899',24);
INSERT INTO produto(id, nome, tipo, descricao, preco) VALUES (nextval('hibernate_sequence'), 'Livro - Esaú e Jacó ','Livro','Autor: Machado de Assis, ano: 1904',1);
INSERT INTO produto(id, nome, tipo, descricao, preco) VALUES (nextval('hibernate_sequence'), 'Livro - Memorial de Aires ','Livro','Autor: Machado de Assis, ano: 1908',70);
INSERT INTO produto(id, nome, tipo, descricao, preco) VALUES (nextval('hibernate_sequence'), 'Livro - O Hobbit ou Lá e de Volta Outra Vez','Livro','Autor: J. R. R. Tolkien, ano: 1937',41);
INSERT INTO produto(id, nome, tipo, descricao, preco) VALUES (nextval('hibernate_sequence'), 'Livro - Sobre Histórias de Fadas','Livro','Autor: J. R. R. Tolkien, ano: 1945',6);
INSERT INTO produto(id, nome, tipo, descricao, preco) VALUES (nextval('hibernate_sequence'), 'Livro - Mestre Gil de Ham','Livro','Autor: J. R. R. Tolkien, ano: 1949',42);
INSERT INTO produto(id, nome, tipo, descricao, preco) VALUES (nextval('hibernate_sequence'), 'Livro - A Sociedade do Anel','Livro','Autor: J. R. R. Tolkien, ano: 1954',12);
INSERT INTO produto(id, nome, tipo, descricao, preco) VALUES (nextval('hibernate_sequence'), 'Livro - As Duas Torres','Livro','Autor: J. R. R. Tolkien, ano: 1954',50);
INSERT INTO produto(id, nome, tipo, descricao, preco) VALUES (nextval('hibernate_sequence'), 'Livro - O Retorno do Rei','Livro','Autor: J. R. R. Tolkien, ano: 1955',90);
INSERT INTO produto(id, nome, tipo, descricao, preco) VALUES (nextval('hibernate_sequence'), 'Livro - As Aventuras de Tom Bombadil','Livro','Autor: J. R. R. Tolkien, ano: 1962',13);
INSERT INTO produto(id, nome, tipo, descricao, preco) VALUES (nextval('hibernate_sequence'), 'Livro - Árvore e Folha','Livro','Autor: J. R. R. Tolkien, ano: 1964',41);
INSERT INTO produto(id, nome, tipo, descricao, preco) VALUES (nextval('hibernate_sequence'), 'Livro - A Última Canção de Bilbo','Livro','Autor: J. R. R. Tolkien, ano: 1966',86);
INSERT INTO produto(id, nome, tipo, descricao, preco) VALUES (nextval('hibernate_sequence'), 'Livro - Ferreiro de Bosque Grande','Livro','Autor: J. R. R. Tolkien, ano: 1967',24);
INSERT INTO pedido(id, statusPedido, pedido_total, usuario_id, data) VALUES (nextval('hibernate_sequence'), 'aberto', 11, 2, '2001-01-01');
INSERT INTO dados_pessoais(id,nome, dataNascimento, cpf) VALUES (nextval('hibernate_sequence'), 'nome sobrenome22', '2000-01-11', '12345678902');
INSERT INTO usuario(id,username, password, email, dadosPessoais_id) VALUES (nextval('hibernate_sequence'), 'username1', '1234', 'username1@provedor.com', 24);


INSERT INTO produto_pedido(id, valor_total, quantidadeProduto,produto_id, pedido_id) VALUES (nextval('hibernate_sequence'),11,1,3, 23);

