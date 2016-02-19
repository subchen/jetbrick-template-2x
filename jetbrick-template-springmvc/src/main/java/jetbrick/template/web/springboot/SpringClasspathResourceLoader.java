/**
 * Copyright 2013-2016 Guoqiang Chen, Shanghai, China. All rights reserved.
 *
 *   Author: Guoqiang Chen
 *    Email: subchen@gmail.com
 *   WebURL: https://github.com/subchen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jetbrick.template.web.springboot;

import jetbrick.io.resource.AbstractResource;
import jetbrick.io.resource.Resource;
import jetbrick.io.resource.ResourceNotFoundException;
import jetbrick.template.loader.AbstractResourceLoader;
import jetbrick.util.PathUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class SpringClasspathResourceLoader extends AbstractResourceLoader {

    private final ResourceLoader springResourceLoader;

    public SpringClasspathResourceLoader() {
        super.root = "";
        super.reloadable = false;
        this.springResourceLoader = new DefaultResourceLoader();
    }

    @Override
    public final void setReloadable(boolean reloadable) {
        if (reloadable) {
            final String message = String.format("%s cannnot be reloadable.", SpringClasspathResourceLoader.class.getName());
            throw new UnsupportedOperationException(message);
        }
        super.reloadable = false;
    }

    @Override
    public Resource load(String name) {
        String path = PathUtils.concat(super.getRoot(), name);
        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        org.springframework.core.io.Resource springResource = springResourceLoader.getResource("classpath:" + path);
        SpringClasspathResource resource = new SpringClasspathResource(springResource);
        if (!resource.exist()) {
            return null;
        } else {
            resource.setRelativePathName(name);
            return resource;
        }
    }

    private static class SpringClasspathResource extends AbstractResource {

        private final org.springframework.core.io.Resource springResource;

        public SpringClasspathResource(org.springframework.core.io.Resource springResource) {
            this.springResource = springResource;
        }

        @Override
        public InputStream openStream() throws ResourceNotFoundException {
            try {
                return springResource.getInputStream();
            } catch (IOException e) {
                throw new IllegalStateException();
            }
        }

        @Override
        public URL getURL() {
            try {
                return springResource.getURL();
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        public boolean exist() {
            return springResource.exists();
        }

        @Override
        public boolean isDirectory() {
            try {
                return springResource.getFile().isDirectory();
            } catch (IOException e) {
                return false;
            }
        }

        @Override
        public boolean isFile() {
            try {
                return springResource.getFile().isFile();
            } catch (IOException e) {
                return false;
            }
        }

        @Override
        public long length() {
            if (getURL() == null) {
                return -1;
            }
            try {
                return getURL().openConnection().getContentLength();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override
        public long lastModified() {
            // you never think about reloading...
            // sorry. this code is dirty.
            return Long.MAX_VALUE;
        }
    }
}
