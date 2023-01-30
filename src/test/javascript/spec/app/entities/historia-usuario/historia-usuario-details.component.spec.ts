/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import HistoriaUsuarioDetailComponent from '@/entities/historia-usuario/historia-usuario-details.vue';
import HistoriaUsuarioClass from '@/entities/historia-usuario/historia-usuario-details.component';
import HistoriaUsuarioService from '@/entities/historia-usuario/historia-usuario.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('HistoriaUsuario Management Detail Component', () => {
    let wrapper: Wrapper<HistoriaUsuarioClass>;
    let comp: HistoriaUsuarioClass;
    let historiaUsuarioServiceStub: SinonStubbedInstance<HistoriaUsuarioService>;

    beforeEach(() => {
      historiaUsuarioServiceStub = sinon.createStubInstance<HistoriaUsuarioService>(HistoriaUsuarioService);

      wrapper = shallowMount<HistoriaUsuarioClass>(HistoriaUsuarioDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { historiaUsuarioService: () => historiaUsuarioServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundHistoriaUsuario = { id: 123 };
        historiaUsuarioServiceStub.find.resolves(foundHistoriaUsuario);

        // WHEN
        comp.retrieveHistoriaUsuario(123);
        await comp.$nextTick();

        // THEN
        expect(comp.historiaUsuario).toBe(foundHistoriaUsuario);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundHistoriaUsuario = { id: 123 };
        historiaUsuarioServiceStub.find.resolves(foundHistoriaUsuario);

        // WHEN
        comp.beforeRouteEnter({ params: { historiaUsuarioId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.historiaUsuario).toBe(foundHistoriaUsuario);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
