<template>
  <div>
    <b-dropdown-item to="/historia-usuario">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.historiaUsuario')">Historia Usuario</span>
    </b-dropdown-item>
    <b-dropdown-item to="/tarea">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.tarea')">Tarea</span>
    </b-dropdown-item>
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>
