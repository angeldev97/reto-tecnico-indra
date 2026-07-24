ALTER TABLE producto RENAME COLUMN stock_disponible TO stock;
ALTER TABLE producto ADD COLUMN sku VARCHAR(50);
ALTER TABLE producto ADD COLUMN marca VARCHAR(100);
ALTER TABLE producto ADD COLUMN categoria VARCHAR(100);

UPDATE producto SET sku = 'PER-KBRGB', marca = 'Keychron', categoria = 'Periféricos' WHERE nombre = 'Teclado mecánico';
UPDATE producto SET sku = 'PER-MSINL', marca = 'Logi', categoria = 'Periféricos' WHERE nombre = 'Mouse inalámbrico';
UPDATE producto SET sku = 'MON-24FHD', marca = 'ViewMax', categoria = 'Monitores' WHERE nombre = 'Monitor 24"';
UPDATE producto SET sku = 'AUD-USBMIC', marca = 'SoundOne', categoria = 'Audio' WHERE nombre = 'Audífonos USB';
UPDATE producto SET sku = 'PER-WC1080', marca = 'Nextcam', categoria = 'Periféricos' WHERE nombre = 'Webcam Full HD';

ALTER TABLE producto ALTER COLUMN sku SET NOT NULL;
ALTER TABLE producto ALTER COLUMN marca SET NOT NULL;
ALTER TABLE producto ALTER COLUMN categoria SET NOT NULL;
